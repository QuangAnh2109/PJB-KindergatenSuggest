//    package fa.appcode.web.controller;
//    import jakarta.servlet.ServletException;
//    import jakarta.servlet.http.HttpServletRequest;
//    import jakarta.servlet.http.HttpServletResponse;
//    import org.springframework.security.core.Authentication;
//    import org.springframework.security.core.GrantedAuthority;
//    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//    import org.springframework.stereotype.Component;
//
//    import java.io.IOException;
//    import java.util.Collection;
//
//    @Component
//    public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//        @Override
//        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//            String redirectUrl = "/home";
//            for (GrantedAuthority authority : authorities) {
//                String role = authority.getAuthority();
//                if (role.equals("Admin")) {
//                    redirectUrl = "/school-owner/request-list";
//                    break;
//                } else if (role.equals("School owner")) {
//                    redirectUrl = "/school-owner/request-list";
//                    break;
//                } else if (role.equals("Parent")) {
//                    redirectUrl = "public/home";
//                    break;
//                }
//            }
//            response.sendRedirect(request.getContextPath() + redirectUrl);
//        }
//    }
package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    AccountService accountService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();

        AccountInfo accountInfo = accountService.findByEmail(email);

        if (accountInfo != null && accountInfo.getDatetimeChangePass() == null) {
            response.sendRedirect(request.getContextPath() + "auth/change-password");
            return;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "public/home";

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals(Constant.ADMIN_ROLE)) {
                redirectUrl = "/manager/request-list";
                break;
            } else if (role.equals(Constant.SCHOOL_OWNER_ROLE)) {
                redirectUrl = "/manager/request-list";
                break;
            } else if (role.equals(Constant.PARENT_ROLE)) {
                redirectUrl = "/public/home";
                break;
            }
        }
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
