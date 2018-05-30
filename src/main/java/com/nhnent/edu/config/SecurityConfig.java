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
                .requiresChannel()
                    /* TODO : #2 관리툴/프로젝트/공개프로젝트 페이지는 secure로 접속되도록 설정해주세요. */
                    .anyRequest().requiresInsecure()
                    .and()
                .authorizeRequests()
                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    /* TODO : #3 공개 프로젝트 URL은 (`/public-project/**`) ADMIN 이나 MEMBER 권한이 있을 때 접근 가능하도록 설정해주세요. */
                    .antMatchers("/project/**").authenticated()
                    .antMatchers("/redirect-index").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .loginPage("/login/form")
                    .usernameParameter("name")
                    .passwordParameter("pwd")
                    .loginProcessingUrl("/login/process")
                    .failureHandler(loginFailureHandler())
                    .and()
                .logout()
                    .and()
                .headers()
                    .defaultsDisabled()
                    /* TODO : #4 Security HTTP Response header 중 `X-Frame-Options` 헤더의 값을 SAMEORIGIN으로 설정해주세요. */
                    .and()
                .exceptionHandling()
                    /* TODO : #9 custom 403 에러 페이지(`/error/403`)를 설정해주세요. */
                    .and()
                .csrf()
                    .disable();
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
        authProvider.setHideUserNotFoundExceptions(false);

        return authProvider;
    }

    @Bean
    public CustomLoginFailureHandler loginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

}
