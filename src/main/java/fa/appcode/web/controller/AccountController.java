package fa.appcode.web.controller;

import fa.appcode.common.utils.ValidateUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.impl.CityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CityServiceImpl cityServiceImpl;
    @Autowired
    GlobalConfig globalConfig;

    @GetMapping("/auth/view-account")
    public String viewAccount(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AccountInfo accountInfo = accountService.findByEmail(email);
        if (accountInfo != null) {
            model.addAttribute("accountInfo", accountInfo);
        }
        model.addAttribute("citys", cityServiceImpl.findAllByNoDelete());
        return "user_side/view-account";
    }

    @PostMapping("/auth/update-account")
    public String updateAccount(@ModelAttribute("accountInfo") AccountInfo accountInfo, Model model, RedirectAttributes redirectAttributes) {
        AccountInfo existingAccount = accountService.findByEmail(accountInfo.getEmail());
        if (existingAccount == null) {
            throw new RuntimeException("Account not found!");
        }
        if (ValidateUtils.validatePhone(accountInfo.getPhone())) {
            model.addAttribute("phoneFail", "enter a valid phone number");
            return "user_side/view-account";
        } else if (accountService.findAccountInfoByPhone(accountInfo.getPhone()) != null) {
            model.addAttribute("phoneFail", "This phone number is already in use");
            return "user_side/view-account";
        }
        existingAccount.setFullName(accountInfo.getFullName());
        existingAccount.setPhone(accountInfo.getPhone());
        existingAccount.setDob(accountInfo.getDob());
        existingAccount.setUpdateTime(Instant.now());
        existingAccount.setCity(accountInfo.getCity());
        existingAccount.setDistrict(accountInfo.getDistrict());
        existingAccount.setWard(accountInfo.getWard());
        existingAccount.setAddress(accountInfo.getAddress());
        accountService.save(existingAccount);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/auth/view-account";
    }
}

