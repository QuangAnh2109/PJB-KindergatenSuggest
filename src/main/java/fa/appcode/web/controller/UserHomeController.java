package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/")
@Controller
public class UserHomeController {
    @GetMapping()
    public String homepage(Principal principal, Model model){
        model.addAttribute("user", principal);
        return "user_side/index";
    }
}