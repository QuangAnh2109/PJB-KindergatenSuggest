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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("public/register")
@RequiredArgsConstructor
public class RegisterController {

    private static final Logger LOGGER = Log4jUtils.getLogger(RegisterController.class);
    private final AccountService accountService;
    private final GlobalConfig globalConfig;

    /**
     * Handles GET request to show the registration page.
     * @param model Model object to pass data to the view
     * @return Registration page view name
     */
    @GetMapping
    public String showRegisterPage(Model model) {
        LOGGER.info("Accessing registration page");
        // Add an empty AccountVo object to the model for form binding
        model.addAttribute("accountVo", new AccountVo());
        // Return the registration page view
        return Constant.REGISTER_PAGE;
    }

    /**
     * Handles POST request to process user registration.
     * @param accountVo The form data submitted by the user
     * @param model     Model object to pass messages or errors to the view
     * @return Registration page view name
     */
    @PostMapping
    public String processRegister(@ModelAttribute("accountVo") @Valid AccountVo accountVo,
                                  Model model) {
        LOGGER.info("Processing registration for email: {}", accountVo.getEmail());
        Map<String, String> registrationErrors = accountService.handleRegisterProcess(accountVo);
        if (!registrationErrors.isEmpty()) {
            model.addAllAttributes(registrationErrors);
            return Constant.REGISTER_PAGE;
        }
        model.addAttribute("successMessage", globalConfig.getRegisterSuccess());
        return Constant.REGISTER_PAGE;
    }
    /**
     * Handles GET request for email verification.
     * @param token Verification token from the URL
     * @param model Model object to pass messages to the view
     * @return Verification result page view name
     */
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token, Model model) {
        LOGGER.info("Verifying account with token: {}", token);

        // Call service method to verify the account using the token
        boolean isVerified = accountService.verifyAccount(token);
        if (!isVerified) {
            model.addAttribute("alreadyVerified", globalConfig.getAlreadyVerification());
        } else {
            model.addAttribute("activeSuccess", globalConfig.getActiveSuccess());
        }
        // Return the verification result page
        return Constant.VERIFY_ACCOUNT_PAGE;
    }
}
