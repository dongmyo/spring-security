package com.nhnent.edu.config;

import com.nhnent.edu.security.CustomUserDetailsService;
import com.nhnent.edu.security.PreAuthFilter;
import com.nhnent.edu.security.PreAuthLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(preAuthAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(preAuthFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .authorizeRequests()
                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .antMatchers("/public-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                    .antMatchers("/project/**").authenticated()
                    .antMatchers("/redirect-index").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(preAuthLogoutSuccessHandler())
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                    .and()
                .csrf()
                    .disable();
    }

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PreAuthFilter preAuthFilter() throws Exception {
        PreAuthFilter preAuthFilter = new PreAuthFilter();
        preAuthFilter.setAuthenticationManager(authenticationManagerBean());

        return preAuthFilter;
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider authProvider = new PreAuthenticatedAuthenticationProvider();
        authProvider.setPreAuthenticatedUserDetailsService(preAuthUserDetailsService());

        return authProvider;
    }

    @Bean
    public UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> preAuthUserDetailsService() {
        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
        wrapper.setUserDetailsService(customUserDetailsService());

        return wrapper;
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PreAuthLogoutSuccessHandler preAuthLogoutSuccessHandler() {
        return new PreAuthLogoutSuccessHandler();
    }

}