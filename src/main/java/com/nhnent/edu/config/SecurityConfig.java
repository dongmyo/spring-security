package com.nhnent.edu.config;

import com.nhnent.edu.security.CustomLoginFailureHandler;
import com.nhnent.edu.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired(required = false)
    CustomUserDetailsService customUserDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/public-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                .antMatchers("/project/**").authenticated()
                .antMatchers("/redirect-index").authenticated()
                .anyRequest().permitAll()
                .and()
                // TODO : #1 커스텀 로그인
                .formLogin()
                    .loginPage("/login/form")
                    .usernameParameter("name")
                    .passwordParameter("pwd")
                    .loginProcessingUrl("/login/process")
                    .failureHandler(loginFailureHandler())
                    .and()
                .logout()
                .and()
                .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);

        return authProvider;
    }

    @Bean
    public CustomLoginFailureHandler loginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

}
