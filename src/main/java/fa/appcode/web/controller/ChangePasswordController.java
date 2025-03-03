package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.ValidateUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ChangePasswordController {
    private final AccountService accountService;
    private final GlobalConfig globalConfig;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/auth/change-password")
    public String showChangePasswordPage() {
        return Constant.CHANGE_PASSWORD_PAGE;
    }

    @PostMapping("/auth/change-password")
    public String changePasswordProcess(@RequestParam("oldPassword") String oldPassword,
                                        @RequestParam("newPassword") String newPassword,
                                        @RequestParam("confirmPassword") String confirmPassword,
                                        Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            AccountInfo accountInfo = accountService.findByEmail(email);

            boolean isPasswordMatch = passwordEncoder.matches(oldPassword, accountInfo.getPassword().replace("{bcrypt}", ""));
            if (!isPasswordMatch) {
                model.addAttribute("passwordWrong", globalConfig.getOldPasswordWrong());
            } else if (!ValidateUtils.validatePass(newPassword)) {
                model.addAttribute("errorPassword", globalConfig.getValidatePassword());
            } else if (newPassword.equals(oldPassword)) {
                model.addAttribute("errorPassword", globalConfig.getNewPasswordWrong());
            } else if (!confirmPassword.equals(newPassword)) {
                model.addAttribute("notMatch", globalConfig.getPasswordNotMatch());
            } else {
                accountService.updatePassword(email, newPassword);
                SecurityContextHolder.getContext().setAuthentication(null);

                model.addAttribute("successUpdate", globalConfig.getUpdateSuccess());
                return Constant.CHANGE_PASSWORD_PAGE;
            }
            return Constant.CHANGE_PASSWORD_PAGE;
        } catch (Exception e) {
            model.addAttribute("exceptionError", globalConfig.getAnErrorOccur());
            return Constant.CHANGE_PASSWORD_PAGE;
        }
    }
}
