//package fa.appcode.web.controller;
//
//import fa.appcode.common.vo.AccountVo;
//import fa.appcode.entities.AccountInfo;
//import fa.appcode.services.AccountService;
//import fa.appcode.services.EmailService;
//import fa.appcode.services.impl.VerificationService;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Random;
//
//@Controller
//@RequestMapping("/register")
//public class AccountController {
//
//    private final String userRegister = "user_side/register";
//    private final String verifyPage = "user_side/verify-email";
//
//    @Autowired
//    private AccountService accountService;
//    @Autowired
//    private EmailService emailService;
//    @Autowired
//    private VerificationService verificationService;
//
//    private final Map<String, AccountVo> pendingAccounts = new HashMap<>();
//
//    @GetMapping
//    public String register(Model model) {
//        model.addAttribute("accountVo", new AccountVo());
//        return userRegister;
//    }
//    @PostMapping
//    public String processRegister(@ModelAttribute("accountVo") @Valid AccountVo accountVo,
//                                  BindingResult bindingResult,
//                                  Model model) {
//
//        if (bindingResult.hasErrors()) {
//            return userRegister;
//        }
//
//        if (accountService.existsByEmail(accountVo.getEmail())) {
//            model.addAttribute("emailError", "Email already exists!");
//            return userRegister;
//        }
//
//        if (!accountVo.getPassword().equals(accountVo.getConfirmPassword())) {
//            model.addAttribute("confirmPasswordError", "Passwords do not match!");
//            return userRegister;
//        }
//
//        // Tạo mã OTP
//        String otpCode = verificationService.generateOtp(accountVo.getEmail());
//
//        // Gửi OTP đến email
//        emailService.sendEmail(accountVo.getEmail(),"Verify you email", otpCode);
//
//        // Lưu thông tin đăng ký tạm thời
//        pendingAccounts.put(accountVo.getEmail(), accountVo);
//
//        model.addAttribute("email", accountVo.getEmail());
//        return verifyPage;
//    }
//
//    @PostMapping("/verify")
//    public String verifyOtp(@RequestParam String email,
//                            @RequestParam String otp,
//                            Model model) {
//
//        if (!verificationService.validateOtp(email, otp)) {
//            model.addAttribute("error", "Invalid or expired OTP!");
//            return verifyPage;
//        }
//
//        AccountVo accountVo = pendingAccounts.remove(email);
//        if (accountVo == null) {
//            model.addAttribute("error", "Session expired, please register again.");
//            return userRegister;
//        }
//
//        AccountInfo accountInfo = new AccountInfo();
//        accountInfo.setFullName(accountVo.getFullName());
//        accountInfo.setEmail(accountVo.getEmail());
//        accountInfo.setPhone(accountVo.getPhone());
//        String encodedPassword = accountService.encodePassword(accountVo.getPassword());
//        accountInfo.setPassword("{bcrypt}" + encodedPassword);
//        accountInfo.setStatusId(1);
//        accountInfo.setRoleId(3);
//        accountInfo.setRecordNo(1);
//        accountInfo.setCreateId("web_system");
//        accountInfo.setUpdateId("web_system");
//        accountInfo.setCreateTime(Instant.now());
//        accountInfo.setUpdateTime(Instant.now());
//        accountInfo.setAddress("null");
//        accountService.save(accountInfo);
//        System.out.println("Saved account: " + accountVo.getFullName());
//        return "redirect:/showMyLoginPage";
//    }
////    @PostMapping("/resend")
////    public String resendOtp(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
////        // Kiểm tra email có tồn tại không
////        Optional<AccountVo> userOptional = accountService.(email);
////        if (userOptional.isEmpty()) {
////            redirectAttributes.addFlashAttribute("error", "Email không tồn tại!");
////            return "redirect:/register/verify";
////        }
////
////        // Tạo mã OTP mới
////        String newOtp = otpService.generateOtp(email);
////
////        // Gửi OTP qua email
////        emailService.accountService(email, newOtp);
////
////        // Lưu OTP mới vào session hoặc database
////        otpService.storeOtp(email, newOtp);
////
////        redirectAttributes.addFlashAttribute("message", "Mã OTP mới đã được gửi!");
////        return "redirect:/register/verify?email=" + email;
////    }
//
//}
package fa.appcode.web.controller;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import fa.appcode.services.impl.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class AccountController {

    private final String REGISTER_PAGE = "user_side/register";
    private final String VERIFY_PAGE = "user_side/verify-email";
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationService verificationService;
    private final Map<String, AccountVo> pendingAccounts = new HashMap<>();
    @GetMapping
    public String register(Model model) {
        model.addAttribute("accountVo", new AccountVo());
        return REGISTER_PAGE;
    }
    @PostMapping
    public String processRegister(@ModelAttribute("accountVo") @Valid AccountVo accountVo,
                                  BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            return REGISTER_PAGE;
        }
        if (accountService.existsByEmail(accountVo.getEmail())) {
            model.addAttribute("emailError", globalConfig.getEmailExist());
            return REGISTER_PAGE;
        }
        if (!accountVo.getPassword().equals(accountVo.getConfirmPassword())) {
            model.addAttribute("confirmPasswordError",globalConfig.getPasswordNotMatch());
            return REGISTER_PAGE;
        }
        String otpCode = verificationService.generateOtp(accountVo.getEmail());
        emailService.sendEmail(accountVo.getEmail(), "Email Authentication", "Your OTP CODE IS : " + otpCode);
        pendingAccounts.put(accountVo.getEmail(), accountVo);
        model.addAttribute("email", accountVo.getEmail());
        return VERIFY_PAGE;
    }
    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            Model model) {

        if (!verificationService.validateOtp(email, otp)) {
            model.addAttribute("errorOTP", "Your OTP is expired or incorrect!");
            return VERIFY_PAGE;
        }
        AccountVo accountVo = pendingAccounts.remove(email);
        if (accountVo == null) {
            model.addAttribute("errorOTP", "Register session is expired. Please try again!");
            return REGISTER_PAGE;
        }
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFullName(accountVo.getFullName());
        accountInfo.setEmail(accountVo.getEmail());
        accountInfo.setPhone(accountVo.getPhone());
        accountInfo.setPassword("{bcrypt}" + accountService.encodePassword(accountVo.getPassword()));
        accountInfo.setStatusId(1);
        accountInfo.setRoleId(3);
        accountInfo.setRecordNo(1);
        accountInfo.setCreateId("web_system");
        accountInfo.setUpdateId("web_system");
        accountInfo.setCreateTime(Instant.now());
        accountInfo.setUpdateTime(Instant.now());
        accountInfo.setAddress("null");
        accountService.save(accountInfo);
        return "redirect:/showMyLoginPage";
    }
    @PostMapping("/resend")
    public String resendOtp(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        if (!pendingAccounts.containsKey(email)) {
            redirectAttributes.addFlashAttribute("error", globalConfig.getEmailNotExist());
            return "redirect:/register";
        }
        String newOtp = verificationService.generateOtp(email);
        emailService.sendEmail(email, "OTP CODE SENDER", "Your New OTP Code: " + newOtp);
        redirectAttributes.addFlashAttribute("message", "New Otp code is sent!");
        return "redirect:/register/verify?email=" + email;
    }
}
