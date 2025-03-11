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

    //
//    @PostMapping("/view-account")
//    public String updateAccount(@ModelAttribute("accountInfo") AccountInfo accountInfo, Model model) {
//        return accountService.updateAccountDetails(accountInfo, model);
//    }
@PostMapping("/view-account")
public String updateAccount(@ModelAttribute("accountInfo") AccountInfo accountInfo, Model model) {
    boolean isUpdated = accountService.updateAccountDetails(accountInfo, model);
    model.addAttribute("cities", cityService.findAllByNoDelete());
    return isUpdated ? "redirect:/auth/view-account?success=true" : Constant.VIEW_ACCOUNT_PAGE;
}

}
