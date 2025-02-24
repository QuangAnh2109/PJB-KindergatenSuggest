package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "user_side/index";
    }
    @GetMapping("/school-owner/request-list")
    public String admin_home() {
        return "admin_side/request-list";
    }

}
