package fa.appcode.web.controller;

import fa.appcode.common.constant.JwtUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
public class ForgotPasswordController {

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AccountService accountService;

    private static final String RESET_PASSWORD_URL = "http://localhost:8080/reset-password?token=";
    private static final String PASSWORD_FORM_URL = "user_side/forgot-password";

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return PASSWORD_FORM_URL;
    }


    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordController.class);

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model) {
        long startTime = System.currentTimeMillis();

        log.info("⏳ Start processing forgot password for: {}", email);

        AccountInfo account = accountService.findByEmail(email);
        if (account == null) {
            model.addAttribute("userNotExist", globalConfig.getEmailNotExist());
            log.info("❌ Email not found. Time taken: {} ms", (System.currentTimeMillis() - startTime));
            return PASSWORD_FORM_URL;
        }

        String token = jwtUtil.generateToken(email);
        String resetLink = RESET_PASSWORD_URL + token;

        log.info("✅ JWT Token created. Time taken: {} ms", (System.currentTimeMillis() - startTime));

        try {
            emailService.sendEmail(email, "Reset Your Password",
                    "Click this link to reset your password: " + resetLink);
            model.addAttribute("message", "A password reset link has been sent to your email.");
        } catch (Exception e) {
            model.addAttribute("emailError", "Failed to send email. Please try again later.");
            log.error("🚨 Email sending failed: {}", e.getMessage());
        }

        log.info("✅ Email sent successfully. Total time taken: {} ms", (System.currentTimeMillis() - startTime));

        return PASSWORD_FORM_URL;
    }

}
