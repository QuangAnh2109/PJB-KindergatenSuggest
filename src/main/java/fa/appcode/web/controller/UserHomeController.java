package fa.appcode.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserHomeController {

    @GetMapping("/admin/home")
    public String adminHome() {
        return "user_side/admin-home";
    }

    @GetMapping("/school-owner/home")
    public String schoolOwnerHome() {
        return "user_side/school-owner";
    }

    @GetMapping("/home")
    public String parentHome() {
        return "user_side/index";
    }

    @GetMapping("/homeless")
    public String defaultHome1() {
        return "user_side/admin-home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "user_side/access-denied";

    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "user_side/forgot-password";
    }

    @GetMapping("/register")
    public String register() {
        System.out.println("User accessed: /register");
        return "user_side/register";
    }
    @GetMapping("/reset-password")
    public String resetPassword() {
        return "user_side/reset-password";
    }
}