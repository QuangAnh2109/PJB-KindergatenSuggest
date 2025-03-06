package fa.appcode.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyRequestController {
    @GetMapping("/parent/my-request")
    public String myRequest(Model model) {
        return "user_side/my-request";
    }
}
