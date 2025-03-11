package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String forgotPasswordProcess(@RequestParam String email, Model model) {
        log.info("Processing forgot password for email: {}", email);
        model.addAttribute("email", email);
        accountService.forgotPasswordProcess(email, model);
        return Constant.FORGOT_PASSWORD_PAGE;
    }
}
