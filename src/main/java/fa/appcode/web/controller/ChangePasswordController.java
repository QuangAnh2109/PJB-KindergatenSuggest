package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.CityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Controller responsible for handling password change operations
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ChangePasswordController {
    private final AccountService accountService;
    private final GlobalConfig globalConfig;
    private final CityService cityService; // Thêm CityService

    /**
     * Display the change password page
     */
    @GetMapping("/change-password")
    public String showChangePasswordPage(Model model) {
        // Redirect to new combined page with tab indicator
        return "redirect:/auth/account-management?tab=password";
    }

    /**
     * Process the password change request
     */
    @PostMapping("/change-password")
    public String changePasswordProcess(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpServletRequest request,
            Model model) {
        Map<String, String> updatePasswordResult = accountService.changePasswordHandle(oldPassword, newPassword, confirmPassword);
        AccountInfo accountInfo = accountService.getCurrentAccountInfo();
        model.addAttribute("accountInfo", accountInfo);
        if (!updatePasswordResult.isEmpty()) {
            for (Map.Entry<String, String> entry : updatePasswordResult.entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        else {
            model.addAttribute("successUpdate", globalConfig.getUpdateSuccess());
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
        }
        model.addAttribute("activeTab", "password");
        return Constant.ACCOUNT_MANAGEMENT_PAGE;
    }
}
