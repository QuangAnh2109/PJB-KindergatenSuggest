package fa.appcode.security;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.config.GlobalConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = Log4jUtils.getLogger(AuthenticationHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String email = request.getParameter("username");
        String error = "error";
        if (exception instanceof UsernameNotFoundException) {
            error = globalConfig.getEmailNotExist();
        } else if (exception instanceof BadCredentialsException) {
            error = globalConfig.getLoginFailed();
            LOGGER.warn("Bad credentials for email {}: {}", email, exception.getMessage());
        } else if (exception instanceof DisabledException) {
            error = globalConfig.getAccountNotActive();
            LOGGER.warn("Account is not active for email {}: {}", email, exception.getMessage());
        } else if (exception instanceof LockedException) {
            error = globalConfig.getAccountDisabled();
            LOGGER.warn("Account locked for email {}: {}", email, exception.getMessage());
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"" + error + "\"}");
    }

}
