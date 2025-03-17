package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("public/register")
@RequiredArgsConstructor
public class RegisterController {

    private static final Logger LOGGER = Log4jUtils.getLogger(RegisterController.class);
    private final AccountService accountService;
    private final GlobalConfig globalConfig;

    @GetMapping
    public String register(Model model) {
        LOGGER.info("Accessing registration page");
        model.addAttribute("accountVo", new AccountVo());
        return Constant.REGISTER_PAGE;
    }

    @PostMapping
    public String processRegister(@ModelAttribute("accountVo") @Valid AccountVo accountVo,
                                  Model model) {
        LOGGER.info("Processing registration for     email: {}", accountVo.getEmail());
        boolean isSuccessRegistration = accountService.processRegister(accountVo);
        if (isSuccessRegistration) {
            model.addAttribute("successMessage", globalConfig.getRegisterSuccess());
        } else {
            Map<String, Object> validationResult = accountService.getValidationResult();
            Optional.ofNullable(validationResult)
                    .map(result -> result.get("errors"))
                    .filter(Map.class::isInstance)
                    .map(map -> (Map<String, String>) map)
                    .ifPresent(errors -> errors.forEach(model::addAttribute));
        }
        return Constant.REGISTER_PAGE;
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token, Model model) {
        LOGGER.info("Verifying account with token: {}", token);
        boolean isVerified = accountService.verifyAccount(token);
        if (!isVerified) {
            model.addAttribute("alreadyVerified", globalConfig.getAlreadyVerification());
        }
        else {
            model.addAttribute("activeSuccess", globalConfig.getActiveSuccess());
        }
        return Constant.VERIFY_ACCOUNT_PAGE;
    }
}
