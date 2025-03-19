package fa.appcode.common.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org. springframework. security. core. Authentication;

public class AuthenticationGet {
    public static String getAccountEmailByAuthen() throws NullPointerException {
        // Get the current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is an admin
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals(Constant.ADMIN_ROLE)) {
                return null;
            }
        }
        return authentication.getName();
    }
}
