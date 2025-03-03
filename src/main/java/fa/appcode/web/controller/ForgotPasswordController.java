package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ForgotPasswordController {
    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private EmailService emailService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenUtils tokenUtils;
    private static final String RESET_PASSWORD_URL = "http://localhost:8080/public/reset-password?token=";

    @GetMapping("/public/forgot-password")
    public String showForgotPasswordForm() {
        return Constant.FORGOT_PASSWORD_PAGE;
    }

    @PostMapping("/public/forgot-password")
    public String forgotPasswordProcess(@RequestParam String email, Model model) {
        try {
            if (email == null) {
                model.addAttribute("userNotExist", globalConfig.getEmailNotExist());
            }
            AccountInfo accountInfo = accountService.findByEmail(email);
            String token = tokenUtils.generateTokenForgot(email,accountInfo.getDatetimeChangePass());
            String resetLink = RESET_PASSWORD_URL + token;
            Map<Placeholder, String> m = Map.of(Placeholder.LINK,resetLink);
            SendMailInfo sendMailInfo = SendMailInfo.builder().toMail(List.of(email)).mailId(Constant.SEND_EMAIL_FORGOT).ccMail(List.of()).detail(m).build();
            emailService.sendEmailToMany(sendMailInfo);
            model.addAttribute("message", globalConfig.getVerifyLinkSend());
        } catch (Exception e) {
            model.addAttribute("emailError", "An error occurred: " + e.getMessage());
        }
        return Constant.FORGOT_PASSWORD_PAGE;
    }

}
