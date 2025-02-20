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
    @GetMapping("/admin/home")
    public String admin_home() {
        return "admin_side/index";
    }
    @GetMapping("/admin/request-list")
    public String request_list() {
        return "admin_side/request-list";
    }
    @GetMapping("/admin/request-list-detail")
    public String request_list_detail() {
        return "admin_side/request-list-detail";
    }

    @GetMapping("/admin/request-reminder")
    public String request_reminder() {
        return "admin_side/request-reminder";
    }
}
