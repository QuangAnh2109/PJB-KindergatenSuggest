package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.common.utils.ValidateUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private static final Logger logger = Log4jUtils.getLogger();

    private final GlobalConfig globalConfig;
    private final TokenUtils tokenUtils;
    private final AccountService accountService;

    @GetMapping("/public/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        try {
            String email = tokenUtils.getEmailFromToken(token);
            AccountInfo account = accountService.findByEmail(email);
            if (account == null || !tokenUtils.isTokenValid(token, account)) {
                model.addAttribute("error", globalConfig.getExpiredLink());
                return Constant.TOKEN_INVALID_PAGE;
            }
            model.addAttribute("token", token);
            return Constant.RESET_PASSWORD_PAGE;
        } catch (Exception e) {
            logger.error("Error showing reset password form: {}", e.getMessage(), e);
            model.addAttribute("error", globalConfig.getAnErrorOccur());
            return Constant.TOKEN_INVALID_PAGE;
        }
    }

    @PostMapping("/public/reset-password")
    public String resetPasswordProcess(@RequestParam String token,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       Model model) {
        String error = null;
        try {
            String email = tokenUtils.getEmailFromToken(token);
            AccountInfo account = accountService.findByEmail(email);

            if (account == null) {
                error = globalConfig.getEmailNotExist();
            } else if (!tokenUtils.isTokenValid(token, account)) {
                error = globalConfig.getExpiredLink();
            } else if (!ValidateUtils.validatePass(newPassword)) {
                error = globalConfig.getValidatePassword();
            } else if (!newPassword.equals(confirmPassword)) {
                error = globalConfig.getPasswordNotMatch();
            } else {
                account.setDatetimeChangePass(Instant.now());
                accountService.updatePassword(email, newPassword);
                model.addAttribute("passwordReset", globalConfig.getPasswordResetSuccess());
            }
        } catch (Exception e) {
            logger.error("Error resetting password: {}", e.getMessage(), e);
            error = globalConfig.getAnErrorOccur();
        }

        if (error != null) {
            model.addAttribute("error", error);
        }
        model.addAttribute("token", token);
        return Constant.RESET_PASSWORD_PAGE;
    }

}
