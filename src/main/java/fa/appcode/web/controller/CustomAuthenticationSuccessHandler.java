package fa.appcode.web.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/home";
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("Admin")) {
                redirectUrl = "/school-owner/request-list";
                break;
            }
            else if(role.equals("School owner")) {
                redirectUrl = "/school-owner/request-list";
                break;
            }
            else if (role.equals("Parent")) {
                redirectUrl = "public/home";
                break;
            }
        }
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
