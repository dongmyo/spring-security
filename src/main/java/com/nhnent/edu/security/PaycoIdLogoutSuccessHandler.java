package com.nhnent.edu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// TODO : #3 LogoutSuccessHandler 구현
public class PaycoIdLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*
         * TODO : #6 구현하세요
         * cf.)
         *  PAYCO ID 연동 가이드 : https://developers.payco.com/guide/development/apply
         *  PAYCO ID Access Token 삭제 API : https://id.payco.com/oauth2.0/logout
         */
        String clientId = oAuth2RestTemplate.getResource().getClientId();

        String token = Optional.ofNullable(authentication)
                .map(OAuth2Authentication.class::cast)
                .map(OAuth2Authentication::getUserAuthentication)
                .map(Authentication::getCredentials)
                .map(String.class::cast)
                .orElse(null);

        if (token != null && token.length() > 0) {
            String paycoIdLogoutUrl = "https://id.payco.com/oauth2.0/logout";

            Map<String, Object> params = new HashMap<>();
            params.put("client_id", clientId);
            params.put("token", token);

            oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(new DefaultOAuth2AccessToken(token));
            oAuth2RestTemplate.getForObject(paycoIdLogoutUrl, Map.class, params);
        }

        request.getSession().invalidate();

        response.sendRedirect("/");
    }

}
