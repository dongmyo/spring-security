package com.nhnent.edu.security;

import org.springframework.beans.factory.annotation.Autowired;
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

// TODO : #10 ResourceServerTokenServices 구현 -->
public class PaycoIdUserInfoTokenServices implements ResourceServerTokenServices {
    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        // TODO : #10 AccessToken을 가지고 "PAYCO ID 회원 정보" (protected resource) 조회
        Map<String, Object> userInfoMap = getPaycoIdUserInfo();
        if (userInfoMap == null || userInfoMap.isEmpty()) {
            throw new InvalidTokenException(accessToken);
        }

        return extractAuthentication(userInfoMap);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported");
    }

    private Map<String, Object> getPaycoIdUserInfo() {
        String resourceUrl = "http://tcc1-alpha-id-bo.payco.com:10003/neid_bo/neidProfileApi/getUserStatusByToken";

        Map<String, String> params = new HashMap<>();
        params.put("access_token", oAuth2RestTemplate.getAccessToken().getValue());
        params.put("client_id", oAuth2RestTemplate.getResource().getClientId());
        params.put("serviceProviderCode", "FRIENDS");
        params.put("version", "1.0");

        return oAuth2RestTemplate.postForObject(resourceUrl, params, Map.class);
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
