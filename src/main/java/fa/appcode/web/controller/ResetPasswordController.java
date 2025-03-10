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

    private final AccountService accountService;

    @GetMapping("/public/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        return (accountService.validateResetToken(token, model) != null)
                ? Constant.RESET_PASSWORD_PAGE
                : Constant.TOKEN_INVALID_PAGE;
    }

    @PostMapping("/public/reset-password")
    public String resetPasswordProcess(@RequestParam String token,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       Model model) {
        return accountService.resetPassword(token, newPassword, confirmPassword, model);
    }

}
