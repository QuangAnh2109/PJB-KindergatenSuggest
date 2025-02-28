//package fa.appcode.web.controller;
//
//import fa.appcode.common.logging.Log4jUtils;
//import fa.appcode.common.utils.Constant;
//import fa.appcode.common.utils.TokenUtils;
//import fa.appcode.common.utils.ValidateUtils;
//import fa.appcode.config.GlobalConfig;
//import fa.appcode.entities.AccountInfo;
//import fa.appcode.services.AccountService;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Instant;
//
//@Controller
//public class ResetPasswordController {
//
//    private static final Logger logger = Log4jUtils.getLogger();
//
//    @Autowired
//    private GlobalConfig globalConfig;
//    @Autowired
//    private TokenUtils tokenUtils;
//    @Autowired
//    private AccountService accountService;
//
//    @GetMapping("/public/reset-password")
//    public String showResetPasswordForm(@RequestParam String token, Model model) {
//        try {
//            String email = tokenUtils.getEmailFromToken(token);
//            AccountInfo account = accountService.findByEmail(email);
//            if (account == null || !tokenUtils.isTokenValid(token, account.getEmail())) {
//                model.addAttribute("error", globalConfig.getExpiredLink());
//                System.out.println("get expired link");
//                return Constant.TOKEN_INVALID_PAGE;
//            }
//            model.addAttribute("token", token);
//            return Constant.RESET_PASSWORD_PAGE;
//        } catch (Exception e) {
//            logger.error("Error showing reset password form for token: {} - {}", token, e.getMessage(), e);
//            model.addAttribute("error", "An unexpected error occurred. Please try again.");
//            return Constant.TOKEN_INVALID_PAGE;
//        }
//    }
//
//    @PostMapping("/public/reset-password")
//    public String resetPassword(@RequestParam String token,
//                                @RequestParam String newPassword,
//                                @RequestParam String confirmPassword,
//                                Model model) {
//        try {
//            String email = tokenUtils.getEmailFromToken(token);
//            AccountInfo account = accountService.findByEmail(email);
//            if (account == null) {
//                model.addAttribute("error", globalConfig.getEmailNotExist());
//                model.addAttribute("token", token);
//                System.out.println("Account not found");
//                return Constant.RESET_PASSWORD_PAGE;
//            } else if (!tokenUtils.isTokenValid(token,account.getEmail())) {
//                System.out.println("email"+account.getEmail());
//                model.addAttribute("error", globalConfig.getExpiredLink());
//                model.addAttribute("token", token);
//                System.out.println("Token is expired");
//                return Constant.TOKEN_INVALID_PAGE;
//            }
//            else if (!ValidateUtils.validatePass(newPassword)) {
//                model.addAttribute("error", globalConfig.getValidatePassword());
//                model.addAttribute("token", token);
//                System.out.println("not valid");
//                return Constant.RESET_PASSWORD_PAGE;
//            } else if (!newPassword.equals(confirmPassword)) {
//                model.addAttribute("error", globalConfig.getPasswordNotMatch());
//                model.addAttribute("token", token);
//                System.out.println("not equal password");
//                return Constant.RESET_PASSWORD_PAGE;
//            }
//            account.setDatetimeChangePass(Instant.now());
//            accountService.updatePassword(account.getEmail(), newPassword);
//            model.addAttribute("passwordReset", globalConfig.getPasswordResetSuccess());
//            return Constant.RESET_PASSWORD_PAGE;
//        } catch (Exception e) {
//            logger.error("Error resetting password for token: {} - {}", token, e.getMessage(), e);
//            model.addAttribute("error", "An unexpected error occurred while resetting your password. Please try again.");
//            return Constant.RESET_PASSWORD_PAGE;
//        }
//    }
//}
package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.common.utils.ValidateUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@Controller
public class ResetPasswordController {

    private static final Logger logger = Log4jUtils.getLogger();

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AccountService accountService;

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
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return Constant.TOKEN_INVALID_PAGE;
        }
    }

    @PostMapping("/public/reset-password")
    public String resetPasswordProcess(@RequestParam String token,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        try {
            String email = tokenUtils.getEmailFromToken(token);
            AccountInfo account = accountService.findByEmail(email);

            if (account == null) {
                model.addAttribute("error", globalConfig.getEmailNotExist());
            } else if (!tokenUtils.isTokenValid(token, account)) {
                model.addAttribute("error", globalConfig.getExpiredLink());
            } else if (!ValidateUtils.validatePass(newPassword)) {
                model.addAttribute("error", globalConfig.getValidatePassword());
            } else if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", globalConfig.getPasswordNotMatch());
            } else {
                account.setDatetimeChangePass(Instant.now());
                accountService.updatePassword(email, newPassword);
                model.addAttribute("passwordReset", globalConfig.getPasswordResetSuccess());
                return Constant.RESET_PASSWORD_PAGE;
            }
            model.addAttribute("token", token);
            return Constant.RESET_PASSWORD_PAGE;
        } catch (Exception e) {
            logger.error("Error resetting password: {}", e.getMessage(), e);
            model.addAttribute("error", "An unexpected error occurred while resetting your password. Please try again.");
            return Constant.RESET_PASSWORD_PAGE;
        }
    }
}

