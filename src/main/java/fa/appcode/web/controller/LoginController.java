package fa.appcode.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(){
//            @RequestParam(value = "error", required = false) String error,
//            @RequestParam(value = "email", required = false) String email,
//            @RequestParam(value = "missing", required = false) String missing,
//            Model model) {
        return "user_side/login";
    }
    @GetMapping("parent/change-password")
    public String showChangePasswordPage(){
        return "user_side/change-password";
    }

}
