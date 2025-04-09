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

    // Map to store user's HttpSession by username
    private final ConcurrentHashMap<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    // Map to store CustomUserDetails by username
    private final ConcurrentHashMap<String, CustomUserDetails> userDetailsMap = new ConcurrentHashMap<>();


    public UserSessionService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    // Registers the HttpSession for a user.
    public void registerSession(String username, HttpSession session) {
        sessionMap.put(username, session);
    }

    // Registers the CustomUserDetails for a user.
    public void registerUserDetails(CustomUserDetails userDetails) {
        userDetailsMap.put(userDetails.getUsername(), userDetails);
    }

    //Expires and invalidates all sessions for a given user.
    public void expireUserSessions(String username) {
        CustomUserDetails userDetails = userDetailsMap.get(username);
        if (userDetails != null) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
            if (sessions != null) {
                for (SessionInformation session : sessions) {
                    session.expireNow();
                }
            }
        }
        // And also invalidate the raw HttpSession if it's being tracked
        HttpSession session = sessionMap.get(username);
        if (session != null) {
            session.removeAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            session.invalidate();
        }

        // Clean up tracking maps and security context
        sessionMap.remove(username);
        userDetailsMap.remove(username);
        SecurityContextHolder.clearContext();
    }
}
