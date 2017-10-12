package com.nhnent.edu.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.List;

public class CustomAccessTokenProviderChain extends AccessTokenProviderChain {
    private ClientTokenServices clientTokenServices;


    public CustomAccessTokenProviderChain(List<? extends AccessTokenProvider> chain) {
        super(chain);
    }

    public void setClientTokenServices(ClientTokenServices clientTokenServices) {
        this.clientTokenServices = clientTokenServices;
        super.setClientTokenServices(clientTokenServices);
    }

    @Override
    public OAuth2AccessToken obtainAccessToken(OAuth2ProtectedResourceDetails resource, AccessTokenRequest request) throws UserRedirectRequiredException, AccessDeniedException {
        OAuth2AccessToken accessToken = null;
        OAuth2AccessToken existingToken = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        /*
         * TODO : Anonymous도 access token 가져올 수 있도록 수정
         * cf.)
         * https://stackoverflow.com/questions/41380738/spring-security-oauth2-insufficientauthenticationexception-authentication-is
         * http://forum.spring.io/forum/spring-projects/security/oauth/121682-authentication-is-required-to-obtain-an-access-token-anonymous-not-allowed
         */
        /*
        if (auth instanceof AnonymousAuthenticationToken) {
            if (!resource.isClientOnly()) {
                throw new InsufficientAuthenticationException(
                        "Authentication is required to obtain an access token (anonymous not allowed)");
            }
        }
        */

        if (resource.isClientOnly() || (auth != null && auth.isAuthenticated())) {
            existingToken = request.getExistingToken();
            if (existingToken == null && clientTokenServices != null) {
                existingToken = clientTokenServices.getAccessToken(resource, auth);
            }

            if (existingToken != null) {
                if (existingToken.isExpired()) {
                    if (clientTokenServices != null) {
                        clientTokenServices.removeAccessToken(resource, auth);
                    }
                    OAuth2RefreshToken refreshToken = existingToken.getRefreshToken();
                    if (refreshToken != null) {
                        accessToken = refreshAccessToken(resource, refreshToken, request);
                    }
                }
                else {
                    accessToken = existingToken;
                }
            }
        }
        // Give unauthenticated users a chance to get a token and be redirected

        if (accessToken == null) {
            // looks like we need to try to obtain a new token.
            accessToken = obtainNewAccessTokenInternal(resource, request);

            if (accessToken == null) {
                throw new IllegalStateException(
                        "An OAuth 2 access token must be obtained or an exception thrown.");
            }
        }

        if (clientTokenServices != null
                && (resource.isClientOnly() || auth != null && auth.isAuthenticated())) {
            clientTokenServices.saveAccessToken(resource, auth, accessToken);
        }

        return accessToken;

    }
}
