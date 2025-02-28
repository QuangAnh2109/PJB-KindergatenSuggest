package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("public/register")
public class RegisterController {

    private static final String REGISTER_URL = "http://localhost:8080/public/register/verify?token=";

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping
    public String register(Model model) {
        model.addAttribute("accountVo", new AccountVo());
        return Constant.REGISTER_PAGE;
    }

    @PostMapping
    public String processRegister(@ModelAttribute("accountVo") @Valid AccountVo accountVo,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                return Constant.REGISTER_PAGE;
            } else if (accountService.findByEmail(accountVo.getEmail()) != null) {
                model.addAttribute("emailError", globalConfig.getEmailExist());
                return Constant.REGISTER_PAGE;
            } else if (!accountVo.getPassword().equals(accountVo.getConfirmPassword())) {
                model.addAttribute("confirmPasswordError", globalConfig.getPasswordNotMatch());
                return Constant.REGISTER_PAGE;
            }
            accountService.createAccount(accountVo);
            String token = tokenUtils.generateTokenRegister(accountVo.getEmail());
            emailService.sendEmail(accountVo.getEmail(), "Verify Your Account",
                    "Click this link to verify your account: " + REGISTER_URL + token);
            redirectAttributes.addFlashAttribute("message", "A verification link has been sent to your email.");
            return "redirect:/public/register";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return Constant.REGISTER_PAGE;
        }
    }
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token, Model model) {
        try {
            String email = tokenUtils.getEmailFromToken(token);
            AccountInfo accountInfo = accountService.findByEmail(email);

            if (accountInfo == null || accountInfo.getDatetimeChangePass() != null) {
                model.addAttribute("error", "Account not found or already verified.");
                return Constant.TOKEN_INVALID_PAGE;
            }
            accountInfo.setStatusId(41);
            accountService.save(accountInfo);
            model.addAttribute("message", "Your account has been successfully created. You can now log in.");
            return Constant.VERIFY_ACCOUNT_PAGE;
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred during verification. Please try again later.");
            return Constant.TOKEN_INVALID_PAGE;
        }
    }
}
