package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final AccountService accountService;
    private final Logger logger = Log4jUtils.getLogger(ForgotPasswordController.class);
    private final GlobalConfig globalConfig;

    /**
     * Displays the forgot password form.
     *
     * @return The forgot password page.
     */
    @GetMapping("/public/forgot-password")
    public String showForgotPasswordForm() {
        return Constant.FORGOT_PASSWORD_PAGE;
    }

    /**
     * Processes the forgot password request.
     *
     * @param email The user's email address.
     * @param model The model to store attributes for the view.
     * @return The forgot password page with relevant attributes.
     */
    @PostMapping("/public/forgot-password")
    public String forgotPasswordProcess(@RequestParam String email, Model model) {
        logger.debug("Processing forgot password request for email: {}", email);
        // Calls service method to handle forgot password logic
        Map<String, String> validateError = accountService.handleForgotPassword(email);
        // Add email to the model to retain input value
        model.addAttribute("email", email);
        // If there are validation errors, add them to the model
        if (!validateError.isEmpty()) {
            model.addAttribute("validateError", validateError.get("emailError"));
        }
        // Otherwise, add the reset password link send status
        else {
            model.addAttribute("linkSendStatus", globalConfig.getSendResetPassword());
        }
        // Return the forgot password page
        return Constant.FORGOT_PASSWORD_PAGE;
    }
}
