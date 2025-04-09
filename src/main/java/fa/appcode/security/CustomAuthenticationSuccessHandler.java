package fa.appcode.security;

import fa.appcode.common.utils.Constant;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final AccountService accountService;
    private final UserSessionService userSessionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String email = authentication.getName();
        AccountInfo accountInfo = accountService.findByEmail(email);
        if (accountInfo.getDatetimeChangePass() == null) {
            response.getWriter().write("{\"redirectUrl\": \"" + request.getContextPath() + "/auth/change-password\"}");
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
                redirectUrl = "/manager/school-list";
                break;
            } else if (role.equals(Constant.PARENT_ROLE)) {
                redirectUrl = "/public/home";
                break;
            }
        }
        session.setAttribute("idAccount", accountInfo.getId());
        session.setAttribute("nameAccount", accountInfo.getFullName());

        //Register session and user details for later tracking and control
        userSessionService.registerSession(email, session);
        userSessionService.registerUserDetails((CustomUserDetails) authentication.getPrincipal());

        response.getWriter().write("{\"redirectUrl\": \"" + request.getContextPath() + redirectUrl + "\"}");
    }

}
