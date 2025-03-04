package fa.appcode.web.controller;

import fa.appcode.config.GlobalConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationHandler implements AuthenticationFailureHandler {
    GlobalConfig globalConfig;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        String error = globalConfig.getLoginFailed();
        if (exception instanceof UsernameNotFoundException) {
            error = globalConfig.getEmailNotExist();
        } else if (exception instanceof BadCredentialsException) {
            error = globalConfig.getIncorrectLogin();
        }
        String encodedError = URLEncoder.encode(error, StandardCharsets.UTF_8);
        response.sendRedirect("public/showMyLoginPage?error=" + encodedError);
    }
}
