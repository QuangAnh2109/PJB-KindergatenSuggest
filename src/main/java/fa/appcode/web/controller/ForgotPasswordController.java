//package fa.appcode.web.controller;
//import fa.appcode.common.utils.Constant;
//import fa.appcode.common.utils.TokenUtils;
//import fa.appcode.config.GlobalConfig;
//import fa.appcode.entities.AccountInfo;
//import fa.appcode.services.AccountService;
//import fa.appcode.services.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.*;
//@Controller
//public class ForgotPasswordController {
//    @Autowired
//    private GlobalConfig globalConfig;
//
//    @Autowired
//    private EmailService emailService;
//    @Autowired
//    private AccountService accountService;
//    @Autowired
//    private TokenUtils tokenUtils;
//    private static final String RESET_PASSWORD_URL = "http://localhost:8080/public/reset-password?token=";
//    private static final String PASSWORD_FORM_URL = "user_side/forgot-password";
//
//    @GetMapping("/public/forgot-password")
//    public String showForgotPasswordForm() {
//        return Constant.FORGOT_PASSWORD_PAGE;
//    }
//
//    @PostMapping("/public/forgot-password")
//    public String forgotPassword(@RequestParam String email, Model model) {
//        AccountInfo account = accountService.findByEmail(email);
//        if (account == null) {
//            model.addAttribute("userNotExist", globalConfig.getEmailNotExist());
//            return Constant.FORGOT_PASSWORD_PAGE;
//        }
//        String existingToken = tokenUtils.getExistingTokenIfValid(email);
//        String token = (existingToken != null) ? existingToken : tokenUtils.generateTokenReset(email);
//        System.out.println("token: " + token);
//        String resetLink = RESET_PASSWORD_URL + token;
//        try {
//            emailService.sendEmail(email, "Reset Your Password",
//                    "Click this link to reset your password: " + resetLink);
//            model.addAttribute("message", "A password reset link has been sent to your email.");
//        } catch (Exception e) {
//            model.addAttribute("emailError", "Failed to send email. Please try again later.");
//        }
//
//        return Constant.FORGOT_PASSWORD_PAGE;
//    }
//
//}
package fa.appcode.web.controller;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
public class ForgotPasswordController {
    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private EmailService emailService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenUtils tokenUtils;
    private static final String RESET_PASSWORD_URL = "http://localhost:8080/public/reset-password?token=";

    @GetMapping("/public/forgot-password")
    public String showForgotPasswordForm() {
        return Constant.FORGOT_PASSWORD_PAGE;
    }
    @PostMapping("/public/forgot-password")
    public String forgotPasswordProcess(@RequestParam String email, Model model) {
        try {
            if (email == null) {
                model.addAttribute("userNotExist", globalConfig.getEmailNotExist());
            }
            String token = tokenUtils.generateToken(email);
            String resetLink = RESET_PASSWORD_URL + token;
            emailService.sendEmail(email, "Reset Your Password",
                    "Click this link to reset your password: " + resetLink);
            model.addAttribute("message", "A password reset link has been sent to your email.");
        } catch (Exception e) {
            model.addAttribute("emailError", "An error occurred: " + e.getMessage());
        }
        return Constant.FORGOT_PASSWORD_PAGE;
    }

}
