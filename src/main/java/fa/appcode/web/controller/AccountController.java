package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.CityService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/account-management")
    public String accountManagement(Model model) {
        AccountInfo accountInfo = accountService.getCurrentAccountInfo();
        model.addAttribute("accountInfo", accountInfo);
        model.addAttribute("cities", cityService.findAllByNoDelete());
        return Constant.ACCOUNT_MANAGEMENT_PAGE;
    }

    @PostMapping("/view-account")
    public String updateAccount(@ModelAttribute("accountInfo") AccountInfo accountInfo,HttpSession session, Model model){
        Map<String, String> accountValidationErrors = accountService.updateAccountProcess(accountInfo);
        if(accountValidationErrors.containsKey("recordChange")) {
            model.addAttribute("recordError", globalConfig.getUpdateFailMessage());
            return Constant.ACCOUNT_MANAGEMENT_PAGE;
        }
        else if (!accountValidationErrors.isEmpty()) {
            model.addAttribute("cities", cityService.findAllByNoDelete());
            model.addAllAttributes(accountValidationErrors);
            return Constant.ACCOUNT_MANAGEMENT_PAGE;
        }
        AccountInfo updatedAccount = accountService.getCurrentAccountInfo();
        session.setAttribute("nameAccount", updatedAccount.getFullName());
        return "redirect:/auth/account-management?success=true";
    }
    @GetMapping("/view-account")
    public String viewAccount(Model model) {
        return "redirect:/auth/account-management";
    }
}
