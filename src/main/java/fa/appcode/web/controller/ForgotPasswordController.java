package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordController {

    private final GlobalConfig globalConfig;
    private final EmailService emailService;
    private final AccountService accountService;
    private final TokenUtils tokenUtils;

    @GetMapping("/public/forgot-password")
    public String showForgotPasswordForm() {
        return Constant.FORGOT_PASSWORD_PAGE;
    }

    @PostMapping("/public/forgot-password")
    public String forgotPasswordProcess(
            @RequestParam String email,
            Model model,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith
    ) {
        log.info("Processing forgot password for email: {}", email);
        model.addAttribute("email", email);
        if (email == null || email.isBlank()) {
            log.warn("Empty email provided");
            model.addAttribute("userNotExist", globalConfig.getEmailNotExist());
            return Constant.FORGOT_PASSWORD_PAGE;
        }
        try {
            AccountInfo accountInfo = accountService.findByEmail(email);
            if (accountInfo == null) {
                log.warn("No account found for email: {}", email);
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
            log.info("Password reset email sent to: {}", email);
            model.addAttribute("message", globalConfig.getSendResetPassword());
        } catch (Exception e) {
            log.error("Error processing forgot password", e);
            model.addAttribute("emailError", "An error occurred: " + e.getMessage());
        }
        return Constant.FORGOT_PASSWORD_PAGE;
    }
}
