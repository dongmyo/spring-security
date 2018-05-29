package com.nhnent.edu.config;

import com.nhnent.edu.security.CustomAccessTokenProviderChain;
import com.nhnent.edu.security.CustomUserDetailsService;
import com.nhnent.edu.security.PaycoIdAuthorizationCodeAccessTokenProvider;
import com.nhnent.edu.security.PaycoIdLogoutSuccessHandler;
import com.nhnent.edu.security.PaycoIdUserInfoTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.Arrays;

@EnableWebSecurity
@EnableOAuth2Client     // OAuth2ClientContextFilter, OAuth2ClientContext
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Autowired
    OAuth2ClientContextFilter oAuth2ClientContextFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /* TODO : OAuth2ClientContextFilter, OAuth2ClientAuthenticationProcessingFilter 필터 추가 */
//                .addFilterAfter...
//                .addFilterBefore...
                /* entry-point-ref */
                .exceptionHandling()
                    .authenticationEntryPoint(oAuth2EntryPoint())
                    .and()
                .authorizeRequests()
                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .antMatchers("/public-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                    .antMatchers("/project/**").authenticated()
                    .antMatchers("/redirect-index").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .logout()
                    /* TODO : LogoutSuccessHandler  등록 */
                    // ...
                    .and()
                .csrf()
                    .disable();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    // OAuth2ProtectedResourceDetails
    @Bean
    public OAuth2ProtectedResourceDetails paycoIdResource() {
        AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
        resource.setClientId("3RDU4G5NI_cxk4VNvSI7");
        resource.setClientSecret("fxVFAe2HjN98DOyrV6kyJVHD");
        resource.setUserAuthorizationUri("https://alpha-id.payco.com/oauth2.0/authorize");
        resource.setAccessTokenUri("https://alpha-id.payco.com/oauth2.0/token");
        resource.setClientAuthenticationScheme(AuthenticationScheme.form);

        return resource;
    }

    // OAuth2RestTemplate
    @Bean
    public OAuth2RestTemplate paycoIdRestTemplate() {
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(paycoIdResource(), oauth2ClientContext);

        restTemplate.setAccessTokenProvider(
                new CustomAccessTokenProviderChain(
                        Arrays.asList(
                                new PaycoIdAuthorizationCodeAccessTokenProvider(),
                                new ImplicitAccessTokenProvider(),
                                new ResourceOwnerPasswordAccessTokenProvider(),
                                new ClientCredentialsAccessTokenProvider()
                        )
                )
        );

        return restTemplate;
    }

    // OAuth2ClientAuthenticationProcessingFilter
    @Bean
    public OAuth2ClientAuthenticationProcessingFilter oauth2ProcessingFilter() {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter("/login/process");
        filter.setRestTemplate(paycoIdRestTemplate());
        filter.setTokenServices(paycoIdTokenServices());

        return filter;
    }

    // ResourceServerTokenServices
    @Bean
    public ResourceServerTokenServices paycoIdTokenServices() {
        return new PaycoIdUserInfoTokenServices();
    }

    // AuthenticationEntryPoint
    @Bean
    public AuthenticationEntryPoint oAuth2EntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login/process");
    }

    // LogoutSuccessHandler
    @Bean
    public PaycoIdLogoutSuccessHandler logoutHandler() {
        return new PaycoIdLogoutSuccessHandler();
    }

}