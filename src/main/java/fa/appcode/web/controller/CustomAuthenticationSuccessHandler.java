package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        AccountInfo accountInfo = accountService.findByEmail(email);
        if (accountInfo.getDatetimeChangePass() == null) {
            response.sendRedirect(request.getContextPath() + "/auth/change-password");
            return;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/public/home";
        HttpSession session = request.getSession();
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals(Constant.ADMIN_ROLE)) {
                redirectUrl = "/manager/request-list";
                break;
            } else if (role.equals(Constant.SCHOOL_OWNER_ROLE)) {
                redirectUrl = "/manager/home";
                break;
            } else if (role.equals(Constant.PARENT_ROLE)) {
                redirectUrl = "/public/home";
                break;
            }
        }
        session.setAttribute("idAccount", accountInfo.getId());
        session.setAttribute("nameAccount", accountInfo.getFullName());
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
