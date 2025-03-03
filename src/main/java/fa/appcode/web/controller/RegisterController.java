package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("public/register")
@RequiredArgsConstructor
@Slf4j
public class RegisterController {


    private final GlobalConfig globalConfig;
    private final AccountService accountService;
    private final EmailService emailService;
    private final TokenUtils tokenUtils;

    private static final String ERROR_ATTRIBUTE = "error";

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
        if (bindingResult.hasErrors()) {
            return Constant.REGISTER_PAGE;
        }
        if (accountService.findByEmail(accountVo.getEmail()) != null) {
            model.addAttribute("emailError", globalConfig.getEmailExist());
            return Constant.REGISTER_PAGE;
        }
        if (!accountVo.getPassword().equals(accountVo.getConfirmPassword())) {
            model.addAttribute("confirmPasswordError", globalConfig.getPasswordNotMatch());
            return Constant.REGISTER_PAGE;
        }
        try {
            accountService.createAccount(accountVo);
            String token = tokenUtils.generateTokenRegister(accountVo.getEmail());
            emailService.sendEmail(accountVo.getEmail(), "Verify Your Account",
                    "Click this link to verify your account: " + Constant.REGISTER_VERIFY_URL + token);
            redirectAttributes.addFlashAttribute("message", globalConfig.getVerifyLinkSend());
            return "redirect:/public/register";
        } catch (Exception e) {
            log.error("Error during registration", e);
            model.addAttribute(ERROR_ATTRIBUTE, globalConfig.getAnErrorOccur());
            return Constant.REGISTER_PAGE;
        }
    }
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token, Model model) {
        try {
            String email = tokenUtils.getEmailFromToken(token);
            AccountInfo accountInfo = accountService.findByEmail(email);

            if (accountInfo == null || accountInfo.getDatetimeChangePass() != null) {
                model.addAttribute(ERROR_ATTRIBUTE, "Account not found or already verified.");
                return Constant.TOKEN_INVALID_PAGE;
            }
            accountInfo.setStatusId(Constant.STATUS_ACTIVE);
            accountService.save(accountInfo);
            model.addAttribute("message", "Your account has been successfully created. You can now log in.");
            return Constant.VERIFY_ACCOUNT_PAGE;
        } catch (Exception e) {
            log.error("Error during account verification", e);
            model.addAttribute(ERROR_ATTRIBUTE, globalConfig.getAnErrorOccur());
            return Constant.TOKEN_INVALID_PAGE;
        }
    }
}

