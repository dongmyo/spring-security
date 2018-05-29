package com.nhnent.edu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.*;

public class PaycoIdUserInfoTokenServices implements ResourceServerTokenServices {
    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> userInfoMap = getPaycoIdUserInfo(accessToken);
        if (userInfoMap == null || userInfoMap.isEmpty()) {
            throw new InvalidTokenException(accessToken);
        }

        return extractAuthentication(userInfoMap);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported");
    }

    private Map<String, Object> getPaycoIdUserInfo(String accessToken) {
        String resourceUrl = "https://dev-apis.krp.toastoven.net/payco/friends/getMemberProfileByFriendsToken.json";

        String clientId = oAuth2RestTemplate.getResource().getClientId();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("client_id", clientId);
        requestHeaders.add("access_token", accessToken);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", clientId);
        requestBody.put("access_token", accessToken);


        HttpEntity<Map> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<Map> response = oAuth2RestTemplate.exchange(
                resourceUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            return Collections.emptyMap();
        }

        Map<String, Object> responseBody = response.getBody();


        return (Map<String, Object>) responseBody.getOrDefault("memberProfile", Collections.emptyMap());
    }

    private OAuth2Authentication extractAuthentication(Map<String, Object> userInfoMap) {
        String userName = (String) userInfoMap.get("id");
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        OAuth2Request request = new OAuth2Request(
                null,
                oAuth2RestTemplate.getResource().getClientId(),
                null,
                true,
                null,
                null,
                null,
                null,
                null
        );

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
        );
        token.setDetails(userInfoMap);

        return new OAuth2Authentication(request, token);
    }
}
