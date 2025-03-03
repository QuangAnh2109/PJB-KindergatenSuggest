package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ForgotPasswordController {

    private final GlobalConfig globalConfig;
    private final EmailService emailService;
    private final AccountService accountService;
    private final TokenUtils tokenUtils;

    // Constructor injection
    public ForgotPasswordController(GlobalConfig globalConfig,
                                    EmailService emailService,
                                    AccountService accountService,
                                    TokenUtils tokenUtils) {
        this.globalConfig = globalConfig;
        this.emailService = emailService;
        this.accountService = accountService;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping("/public/forgot-password")
    public String showForgotPasswordForm() {
        return Constant.FORGOT_PASSWORD_PAGE;
    }

    @PostMapping("/public/forgot-password")
    public String forgotPasswordProcess(@RequestParam String email, Model model) {
        if (email == null || email.isBlank()) {
            model.addAttribute("userNotExist", globalConfig.getEmailNotExist());
            return Constant.FORGOT_PASSWORD_PAGE;
        }
        try {
            AccountInfo accountInfo = accountService.findByEmail(email);
            if (accountInfo == null) {
                model.addAttribute("userNotExist", globalConfig.getEmailNotExist());
                return Constant.FORGOT_PASSWORD_PAGE;
            }
            String token = tokenUtils.generateTokenForgot(email, accountInfo.getDatetimeChangePass());
            String resetLink = Constant.RESET_PASSWORD_URL + token;

            Map<Placeholder, String> link = Map.of(Placeholder.LINK, resetLink);

            SendMailInfo sendMailInfo = SendMailInfo.builder()
                    .toMail(List.of(email))
                    .mailId(Constant.SEND_EMAIL_FORGOT)
                    .ccMail(List.of())
                    .detail(link)
                    .build();

            emailService.sendEmailToMany(sendMailInfo);
            model.addAttribute("message", globalConfig.getSendResetPassword());
        } catch (Exception e) {
            model.addAttribute("emailError", "An error occurred: " + e.getMessage());
        }
        return Constant.FORGOT_PASSWORD_PAGE;
    }
}
