package fa.appcode.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserHomeController {

    @GetMapping("/home")
    public String parentHome(Model model) {
        return "user_side/index";
    }

    @GetMapping("/search")
    public String searchSchool(Model model) {
        return "user_side/search-school";
    }
}