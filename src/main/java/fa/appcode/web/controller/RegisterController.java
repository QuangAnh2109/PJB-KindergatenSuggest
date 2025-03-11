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
                                  BindingResult bindingResult,
                                  Model model) {
        LOGGER.info("Processing registration for account: {}", accountVo.getEmail());

        boolean isSuccess = accountService.processRegister(accountVo, bindingResult, model);

        if (isSuccess) {
            model.addAttribute("message", globalConfig.getVerifyLinkSend());
        }
        return Constant.REGISTER_PAGE;
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token, Model model) {
        LOGGER.info("Verifying account with token: {}", token);
        boolean isVerified = accountService.verifyAccount(token, model);
        if (!isVerified) {
            return Constant.TOKEN_INVALID_PAGE;
        }
        model.addAttribute("message", globalConfig.getActiveSuccess());
        return Constant.VERIFY_ACCOUNT_PAGE;
    }
}
