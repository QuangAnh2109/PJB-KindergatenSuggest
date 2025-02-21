package fa.appcode.controller;

import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
@Controller
@RequestMapping("/register")
public class AccountController {
    private final String userRegister="user_side/register";

    @Autowired
    private AccountService accountService;

    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("accountInfo", new AccountInfo());
        return userRegister;
    }

    @PostMapping
    public String processRegister(@ModelAttribute("accountInfo") @Valid AccountInfo accountInfo,
                                  BindingResult bindingResult,
                                  @RequestParam("repassword") String confirmPassword,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            return userRegister;
        }

        if (accountService.existsByEmail(accountInfo.getEmail())) {
            model.addAttribute("emailError", "Email already exists!");
            return userRegister;
        }

        if (!accountInfo.getPhone().matches("\\d{10,12}")) {
            model.addAttribute("phoneError", "Invalid phone number!");
            return userRegister;
        }

        if (accountInfo.getPassword().length() < 6) {
            model.addAttribute("passwordError", "Password must be at least 6 characters!");
            return userRegister;
        }

        if (!accountInfo.getPassword().equals(confirmPassword)) {
            model.addAttribute("confirmPasswordError", "Passwords do not match!");
            return userRegister;
        }
        String encodedPassword = "";
        encodedPassword=accountService.encodePassword(accountInfo.getPassword());
        accountInfo.setPassword("{bcrypt}" + encodedPassword);
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

}
