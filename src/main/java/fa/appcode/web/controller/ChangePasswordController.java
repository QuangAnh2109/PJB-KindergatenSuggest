package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChangePasswordController {
    private final AccountService accountService;
    private final GlobalConfig globalConfig;

    /**
     * Display the change password page
     */
    @GetMapping("auth/change-password")
    public String showChangePasswordPage() {
        return "redirect:/auth/account-management?tab=password";
    }

    /**
     * Processes the password change request.
     *
     * @param oldPassword     The current password entered by the user.
     * @param newPassword     The new password the user wants to set.
     * @param confirmPassword The confirmation of the new password.
     * @param request         The HTTP request for session management.
     * @param model           The model to store attributes for rendering the view.
     * @return The account management page with appropriate success or error messages.
     */
    @PostMapping("auth/change-password")
    public String changePasswordProcess(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpServletRequest request,
            Model model) {
        AccountInfo accountInfo = accountService.getCurrentAccountInfo();
        model.addAttribute("accountInfo", accountInfo);
        Map<String, String> errors = accountService.changePasswordHandle(oldPassword, newPassword, confirmPassword);
        if (!errors.isEmpty()) {
            model.addAllAttributes(errors);
        } else {
            model.addAttribute("successUpdate", globalConfig.getUpdateSuccess());
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
        }
        model.addAttribute("activeTab", "password");
        return Constant.ACCOUNT_MANAGEMENT_PAGE;
    }
}
