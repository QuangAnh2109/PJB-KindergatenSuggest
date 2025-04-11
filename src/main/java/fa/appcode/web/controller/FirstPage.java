package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstPage {
    @GetMapping("/")
    public String redirectToHomePage() {
        return "redirect:/public/home";
    }
}
