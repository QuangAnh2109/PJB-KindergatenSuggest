    package fa.appcode.web.controller;

    import fa.appcode.config.GlobalConfig;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
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
        @Autowired
        GlobalConfig globalConfig;
        @Override
        public void onAuthenticationFailure(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException exception)
                throws IOException, ServletException {
            String error = "Invalid email or password.";
            if (exception instanceof UsernameNotFoundException) {
                error = globalConfig.getEmailNotExist();}
            else if (exception instanceof DisabledException) {
                error = "Your account is inactive. Check your email for active account.";
            } else if (exception instanceof BadCredentialsException) {
                error = "Invalid email or password.";
            }
            String encodedError = URLEncoder.encode(error, StandardCharsets.UTF_8);
            response.sendRedirect("public/showMyLoginPage?error=" + encodedError);
        }
    }
