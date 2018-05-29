package com.nhnent.edu.config.security;

import com.nhnent.edu.security.CustomLoginFailureHandler;
import com.nhnent.edu.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(20)
@Configuration
public class IdpConfig extends WebSecurityConfigurerAdapter {
    @Autowired(required = false)
    CustomUserDetailsService customUserDetailsService;


    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                    .antMatchers("/login/idp/**", "/logout/idp/**")
                    .and()
                .authorizeRequests()
                    .antMatchers("/login/idp").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .loginPage("/login/idp/form")
                    .usernameParameter("name")
                    .passwordParameter("pwd")
                    .loginProcessingUrl("/login/idp/process")
                    .failureHandler(loginFailureHandler())
                    .and()
                .logout()
                    .logoutUrl("/logout/idp")
                    .logoutSuccessUrl("/")
                    .and()
                .csrf()
                    .disable();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setHideUserNotFoundExceptions(false);

        return authProvider;
    }

    @Bean
    public CustomLoginFailureHandler loginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

}
