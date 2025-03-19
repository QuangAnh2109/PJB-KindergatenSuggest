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

/**
 * Controller responsible for handling password reset functionality.
 */
@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private static final Logger logger = Log4jUtils.getLogger();
    private final GlobalConfig globalConfig;
    private final AccountService accountService;

    /**
     * Handles GET requests to display the reset password page.
     *
     * @param token The reset password token.
     * @param model The Model to store attributes.
     * @return The reset password page if the token is valid; otherwise, the token invalid page.
     */
    @GetMapping("/public/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        // Validate the token and show the appropriate page
        if (accountService.isValidAccountToken(token)){
            model.addAttribute("token", token);
            return Constant.RESET_PASSWORD_PAGE;
        }
        return Constant.TOKEN_INVALID_PAGE;
    }

    /**
     * Handles POST requests to process the password reset.
     *
     * @param token            The reset password token.
     * @param newPassword      The new password entered by the user.
     * @param confirmPassword  The confirmation password entered by the user.
     * @param model            The Model to store attributes.
     * @return The reset password page with success or error messages.
     */
    @PostMapping("/public/reset-password")
    public String resetPasswordProcess(@RequestParam String token,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       Model model) {
        logger.info("Handling reset password request");

        // Process the password reset and retrieve validation errors, if any
        Map<String, String> errors = accountService.handleResetPassword(token, newPassword, confirmPassword);
        // If errors exist, add them all to the model and return the reset password page
        if (!errors.isEmpty()) {
            model.addAllAttributes(errors);
            model.addAttribute("token", token); // Keep the token in the model for reference
            return Constant.RESET_PASSWORD_PAGE;
        }

        // If successful, add a success message and return the reset password page
        model.addAttribute("passwordResetSuccess", globalConfig.getPasswordResetSuccess());
        return Constant.RESET_PASSWORD_PAGE;
    }

}
