package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AccountController {
    private final AccountService accountService;
    private final CityService cityService;
    private final GlobalConfig globalConfig;

    @GetMapping("/view-account")
    public String viewAccount(Model model) {
        AccountInfo accountInfo = accountService.getCurrentAccountInfo();
        if (accountInfo == null) {
            model.addAttribute("error", globalConfig.getNotFound());
            return Constant.VIEW_ACCOUNT_PAGE;
        }
        model.addAttribute("accountInfo", accountInfo);
        model.addAttribute("cities", cityService.findAllByNoDelete());
        return Constant.VIEW_ACCOUNT_PAGE;
    }

    @PostMapping("/view-account")
    public String updateAccount(@ModelAttribute("accountInfo") AccountInfo accountInfo, Model model) {
        model.addAttribute("cities", cityService.findAllByNoDelete());
        Map<String, String> accountValidationErrors = accountService.updateAccountProcess(accountInfo);
        if(!accountValidationErrors.isEmpty()){
            for(Map.Entry<String, String> error : accountValidationErrors.entrySet()){
                model.addAttribute(error.getKey(), error.getValue());
            }
            return Constant.VIEW_ACCOUNT_PAGE;
        }
        return "redirect:/auth/view-account?success=true";
    }
}
