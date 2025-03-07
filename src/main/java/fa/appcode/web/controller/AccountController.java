package fa.appcode.web.controller;

import com.cloudinary.utils.StringUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AccountController {
    private final AccountService accountService;
    private final CityService cityService;
    private final GlobalConfig globalConfig;
    private final MasterDatumService masterDatumService;
    @GetMapping("/view-account")
    public String viewAccount(Model model, @RequestParam(value = "successMessage", required = false) String successMessage) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountInfo accountInfo = accountService.findByEmail(email);

        if (accountInfo != null) {
            model.addAttribute("user", accountInfo);
            model.addAttribute("role", masterDatumService.getMasterByTypeNameAndTypeKey("ROLE",accountInfo.getRoleId()));
        } else {
            model.addAttribute("error", globalConfig.getNotFound());
        }

        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }

        model.addAttribute("citys", cityService.findAllByNoDelete());
        return Constant.VIEW_ACCOUNT_PAGE;
    }

    @PostMapping("/update-account")
    public String updateAccount(@ModelAttribute("user") AccountInfo accountInfo, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("citys", cityService.findAllByNoDelete());
        try {
            if (!Pattern.matches(Constant.PHONE_REGEX, accountInfo.getPhone())) {
                model.addAttribute("phoneFail", globalConfig.getPhoneIsNotValid());
                return Constant.VIEW_ACCOUNT_PAGE;
            }

            AccountInfo currentAccount = accountService.findByEmail(accountInfo.getEmail());
            if (currentAccount == null) {
                model.addAttribute("error", globalConfig.getUserNotFound());
                return Constant.VIEW_ACCOUNT_PAGE;
            }
            if (StringUtils.isEmpty(accountInfo.getAddress())) {
                accountInfo.setCity(currentAccount.getCity());
                accountInfo.setWard(currentAccount.getWard());
                accountInfo.setDistrict(currentAccount.getDistrict());
                accountInfo.setAddress(currentAccount.getAddress());
            } else if (!accountInfo.getPhone().equals(currentAccount.getPhone())) {
                AccountInfo found = accountService.findAccountInfoByPhone(accountInfo.getPhone());
                if (found != null && !found.getEmail().equals(accountInfo.getEmail())) {
                    model.addAttribute("phoneFail", globalConfig.getPhoneIsExist());
                    return Constant.VIEW_ACCOUNT_PAGE;
                }
            }
            accountService.updateAccountInfo(currentAccount, accountInfo);
            redirectAttributes.addFlashAttribute("successMessage", globalConfig.getUpdateSuccess());
            return "redirect:" + Constant.VIEW_ACCOUNT_URL;
        } catch (Exception e) {
            log.error("Error while updating account", e);
            model.addAttribute("error", globalConfig.getAnErrorOccur());
            model.addAttribute("user", accountInfo);
            return Constant.VIEW_ACCOUNT_PAGE;
        }
    }
}
