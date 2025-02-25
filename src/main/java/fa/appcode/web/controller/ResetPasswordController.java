package fa.appcode.web.controller;

import fa.appcode.common.constant.JwtUtils;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ResetPasswordController {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private AccountService accountService;
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        if (!jwtUtil.validateToken(token)) {
            return "redirect:/reset-password-error";
        }
        model.addAttribute("token", token);
        return "user_side/reset-password";
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        if (!jwtUtil.validateToken(token)) {
            return "redirect:/reset-password?error=invalid_token";
        }
        String email = jwtUtil.extractEmail(token);
        AccountInfo account = accountService.findByEmail(email);
        if (account == null) {
            return "redirect:/reset-password?error=user_not_found";
        }
        accountService.updatePassword(email, newPassword);
        return "redirect:/showMyLoginPage";
    }
}
