    package fa.appcode.web.controller;

    import fa.appcode.common.constant.JwtUtils;
    import fa.appcode.entities.AccountInfo;
    import fa.appcode.services.AccountService;
    import fa.appcode.services.EmailService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    @Controller
    public class ForgotPasswordController {

        @Autowired
        private JwtUtils jwtUtil;

        @Autowired
        private EmailService emailService;

        @Autowired
        private AccountService accountService;
        private static final String RESET_PASSWORD_URL = "http://localhost:8080/reset-password?token=";
        @GetMapping("/forgot-password")
        public String showForgotPasswordForm() {
            return "user_side/forgot-password";
        }
        @PostMapping("/forgot-password")
        public String forgotPassword(@RequestParam String email, Model model) {
            AccountInfo account = accountService.findByEmail(email);
            if (account == null) {
                model.addAttribute("userNotExist", "User not found");
                return "user_side/forgot-password";
            }

            String token = jwtUtil.generateToken(email);
            String resetLink = RESET_PASSWORD_URL + token;

            emailService.sendEmail(email, "Reset Your Password",
                    "Click this link to reset your password: " + resetLink);
            model.addAttribute("message", "A password reset link has been sent to your email.");
            return "user_side/forgot-password";
        }

    }
