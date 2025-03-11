package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller responsible for handling password change operations
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ChangePasswordController {
    private final AccountService accountService;

    /**
     * Display the change password page
     */
    @GetMapping("/change-password")
    public String showChangePasswordPage(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("changePasswordTime",
                accountService.findByEmail(email).getDatetimeChangePass());
        return Constant.CHANGE_PASSWORD_PAGE;
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
        boolean isUpdated = accountService.changePasswordProcess(oldPassword, newPassword, confirmPassword, model);
        if (isUpdated) {
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
        }
        return Constant.CHANGE_PASSWORD_PAGE;
    }
}
