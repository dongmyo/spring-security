package com.nhnent.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            /* TODO : #2 secure channel */
            .requiresChannel()
                /* TODO : #3 관리툴/비공개프로젝트/프로젝트 페이지 secure로 설정 */
                /* TODO : #3 管理ツール/プライベートプロジェクト/プロジェクトページsecureに設定 */
//                .antMatchers("/admin/**").requiresSecure()    // Admin Tool
                .anyRequest().requiresInsecure()
                .and()
            .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/private-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                .antMatchers("/project/**").authenticated()
                .antMatchers("/redirect-index").authenticated()
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .and()
            .logout()
                .and()
            .csrf()
            /* TODO : #4 enable csrf */
                .disable()
            /* TODO : #5 response headers */
//                .headers()
//                    .defaultsDisabled()
//                    .cacheControl()
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").authorities("ROLE_ADMIN")
                    .and()
                .withUser("member").password("member").authorities("ROLE_MEMBER")
                    .and()
                .withUser("guest").password("guest").authorities("ROLE_GUEST");
    }

}
