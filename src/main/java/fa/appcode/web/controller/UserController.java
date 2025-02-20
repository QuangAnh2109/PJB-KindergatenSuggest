package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("parent/view-account")
    public String profile(){
        return "user_side/view-account";
    }
}
