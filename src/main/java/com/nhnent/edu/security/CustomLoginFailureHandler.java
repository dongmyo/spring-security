package com.nhnent.edu.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // TODO : #2 login failure handler 구현
        HttpSession session = request.getSession();

        boolean isInvalidUsername = (exception instanceof UsernameNotFoundException);
        boolean isInvalidPassword = (exception instanceof BadCredentialsException);

        session.setAttribute("invalidUsername", isInvalidUsername);
        session.setAttribute("invalidPassword", isInvalidPassword);

        new DefaultRedirectStrategy().sendRedirect(request, response, "/login/form?error");
    }

}
