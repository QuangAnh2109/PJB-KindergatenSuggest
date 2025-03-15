package fa.appcode.web.controller;

import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.repositories.AccountRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class AuthenticationHandler implements AuthenticationFailureHandler {

    private final GlobalConfig globalConfig;
    private final AccountRepository accountRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        String email = request.getParameter("username");
        String error="";
        if (exception instanceof UsernameNotFoundException) {
            error = "Account not found or has been deleted";
        } else if (exception instanceof BadCredentialsException) {
            error = globalConfig.getLoginFailed();
        } else if (exception instanceof DisabledException) {
            error = globalConfig.getAccountDisabled();
        }
        request.getSession().setAttribute("loginError", error);
        request.getSession().setAttribute("email", email);
        response.sendRedirect(request.getContextPath() + "/public/showMyLoginPage");
    }
}
