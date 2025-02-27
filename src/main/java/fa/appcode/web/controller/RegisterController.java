package fa.appcode.web.controller;

import fa.appcode.common.utils.TokenUtils;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import fa.appcode.services.impl.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("public/register")
public class RegisterController {

    private static final String REGISTER_PAGE = "user_side/register";
    private static final String REGISTER_URL = "http://localhost:8080/public/register/verify?token=";

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenUtils tokenUtils;


    private static final Map<String, AccountVo> pendingAccounts = new HashMap<>();
    private static final Map<String, String> tokenToEmail = new HashMap<>();
    @GetMapping
    public String register(Model model) {
        model.addAttribute("accountVo", new AccountVo());
        return REGISTER_PAGE;
    }

    @PostMapping
    public String processRegister(@ModelAttribute("accountVo") @Valid AccountVo accountVo,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return REGISTER_PAGE;
        } else if (accountService.existsByEmail(accountVo.getEmail())) {
            model.addAttribute("emailError", globalConfig.getEmailExist());
            return REGISTER_PAGE;
        } else if (!accountVo.getPassword().equals(accountVo.getConfirmPassword())) {
            model.addAttribute("confirmPasswordError", globalConfig.getPasswordNotMatch());
            return REGISTER_PAGE;
        }

        String token = tokenUtils.generateTokenRegister(accountVo.getEmail());
        String registerLink = REGISTER_URL + token;

        pendingAccounts.put(token, accountVo);
        tokenToEmail.put(token, accountVo.getEmail());

        try {
            emailService.sendEmail(accountVo.getEmail(), "Verify Your Account",
                    "Click this link to verify your account: " + registerLink);
        } catch (Exception e) {
            model.addAttribute("emailError", "Failed to send email. Please try again later.");
            return REGISTER_PAGE;
        }

        redirectAttributes.addFlashAttribute("message", "A verification link has been sent to your email. It will expire in 10 minutes.");
        return "redirect:/public/register";
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token, Model model) {
        if (tokenUtils.isTokenExpired(token)) {
            model.addAttribute("expired", true);
            return "user_side/token_invalid";
        }
        String email = tokenToEmail.get(token);
        if (email == null) {
            model.addAttribute("error", "Invalid or expired token.");
            return "user_side/token_invalid";
        }
        AccountVo accountVo = pendingAccounts.remove(token);
        tokenToEmail.remove(token);
        if (accountVo == null) {
            model.addAttribute("error", "Account not found or already verified.");
            return "user_side/token_invalid";
        }
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFullName(accountVo.getFullName());
        accountInfo.setEmail(accountVo.getEmail());
        accountInfo.setPassword("{bcrypt}" + accountService.encodePassword(accountVo.getPassword()));
        accountInfo.setPhone(accountVo.getPhone());
        accountInfo.setStatusId(41);
        accountInfo.setRoleId(3);
        accountInfo.setImageUrl("null");
        accountInfo.setRecordNo(1);
        accountInfo.setCreateId("WEB_SYSTEM");
        accountInfo.setUpdateId("WEB_SYSTEM");
        accountInfo.setCreateTime(Instant.now());
        accountInfo.setUpdateTime(Instant.now());
        accountService.save(accountInfo);
        model.addAttribute("message", "Your account has been successfully created. You can now log in.");
        return "user_side/verifyAccount";
    }
}
