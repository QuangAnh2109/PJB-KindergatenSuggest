package fa.appcode.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserSessionService {
    private final SessionRegistry sessionRegistry;
    private final ConcurrentHashMap<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    public UserSessionService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void registerSession(String username, HttpSession session) {
        sessionMap.put(username, session);
    }

    public void expireUserSessions(String username) {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof CustomUserDetails userDetails) {
                if (userDetails.getUsername().equals(username)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }

        // Nếu có session đang active, remove nó khỏi SecurityContext và invalidate session
        HttpSession session = sessionMap.get(username);
        if (session != null) {
            session.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            session.invalidate();
            sessionMap.remove(username);
        }

        // Xóa SecurityContext của thread hiện tại (nếu cần)
        SecurityContextHolder.clearContext();
    }
}
