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

    private final AccountService accountService;

    @GetMapping("/public/forgot-password")
    public String showForgotPasswordForm() {
        return Constant.FORGOT_PASSWORD_PAGE;
    }
    @PostMapping("/public/forgot-password")
    public String forgotPasswordProcess(
            @RequestParam String email,
            Model model,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        log.info("Processing forgot password for email: {}", email);
        model.addAttribute("email", email);
        accountService.forgotPasswordProcess(email, model);
        return Constant.FORGOT_PASSWORD_PAGE;
    }
}
