package com.nhnent.edu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// TODO : #2 LogoutSuccessHandler 구현
public class PaycoIdLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    OAuth2RestTemplate oAuth2RestTemplate;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String clientId = oAuth2RestTemplate.getResource().getClientId();
        String token = oAuth2RestTemplate.getAccessToken().getValue();

        String paycoIdLogoutUrl = "https://id.payco.com/oauth2.0/logout";

        Map<String, Object> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("token", token);

        oAuth2RestTemplate.getForObject(paycoIdLogoutUrl, Map.class, params);

        request.getSession().invalidate();

        response.sendRedirect("/");
    }

}
