package fa.appcode.web.controller;

import fa.appcode.common.utils.JwtUtils;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;


@Controller
public class ResetPasswordController {
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private JwtUtils jwtUtil;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AccountService accountService;

    @GetMapping("/public/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
//        if (tokenUtils.isTokenExpired(token)) {
//            return "redirect:/reset-password-error";
//        }
        int id = tokenUtils.checkIdUserToken(token);
        AccountInfo account = accountService.getAccountById(id);

        if (account == null || !tokenUtils.isTokenValid(token, account.getEmail())) {
            return "user_side/token_invalid";
        }

        model.addAttribute("token", token);
        return "user_side/reset-password";
    }


    //    @PostMapping("public/reset-password")
//    public String resetPassword(@RequestParam String token,
//                                @RequestParam String newPassword,
//                                @RequestParam String confirmPassword,
//                                Model model) {
//
//        if (!jwtUtil.validateToken(token)) {
//            model.addAttribute("error", globalConfig.getExpiredLink());
//            return "user_side/reset-password";
//        }
//        String email = jwtUtil.extractEmail(token);
//        AccountInfo account = accountService.findByEmail(email);
//        if (account == null) {
//            model.addAttribute("error", globalConfig.getEmailNotExist());
//            return "user_side/reset-password"   ;
//        }
//        if (!newPassword.equals(confirmPassword)) {
//            model.addAttribute("error", globalConfig.getPasswordNotMatch());
//            return "user_side/reset-password";
//        }
//        if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{12,}$")) {
//            model.addAttribute("error", globalConfig.getValidatePassword());
//            return "user_side/reset-password";
//        }
//        accountService.updatePassword(email, newPassword);
//        model.addAttribute("passwordReset", "Your password has been reset.");
//        return "user_side/reset-password";
//    }
    @PostMapping("public/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {

        if (tokenUtils.isTokenExpired(token)) {
            model.addAttribute("error", globalConfig.getExpiredLink());
            model.addAttribute("token", token);
            return "user_side/reset-password";
        }
        int id = tokenUtils.checkIdUserToken(token);
        AccountInfo account = accountService.getAccountById(id);
        if (account == null) {
            model.addAttribute("error", globalConfig.getEmailNotExist());
            model.addAttribute("token", token);
            return "user_side/reset-password";
        }
    if(!tokenUtils.isTokenValid(token, account.getEmail())) {
        return "user_side/token_invalid";
    }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", globalConfig.getPasswordNotMatch());
            model.addAttribute("token", token);
            return "user_side/reset-password";
        }
        if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{12,}$")) {
            model.addAttribute("error", globalConfig.getValidatePassword());
            model.addAttribute("token", token);
            return "user_side/reset-password";
        }
        account.setDatetimeChangePass(Instant.now());
        accountService.updatePassword(account.getEmail(), newPassword);
        model.addAttribute("passwordReset", "Your password has been reset.");
        return "user_side/reset-password";
    }
}
