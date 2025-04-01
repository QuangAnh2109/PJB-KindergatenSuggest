package fa.appcode.security;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationValidationFilter extends OncePerRequestFilter {
    private final GlobalConfig globalConfig = new GlobalConfig();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("/authenticateTheUser".equals(request.getServletPath()) && "POST".equals(request.getMethod())) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Map<String, String> errors = new HashMap<>();

            // Validate username/email
            if (username == null || username.trim().isEmpty()) {
                errors.put("username", globalConfig.getRequiredField());
            } else if (!Pattern.matches(Constant.EMAIL_REGEX, username)) {
                errors.put("username", globalConfig.getInValidEmail());
            }

            // Validate password
            if (password == null || password.trim().isEmpty()) {
                errors.put("password", globalConfig.getRequiredField());
            } else if (password.length() < 12 || password.length() > 72) {
                errors.put("password", globalConfig.getPasswordLengthLimit());
            }

            // Nếu có lỗi, trả về JSON chứa lỗi cho từng field
            if (!errors.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getWriter(), Map.of("errors", errors));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
