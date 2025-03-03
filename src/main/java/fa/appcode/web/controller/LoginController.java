package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/public/showMyLoginPage")
    public String showMyLoginPage() {
        return "user_side/login";
    }

    @GetMapping("/public/access-denied")
    public String accessDenied() {
        return Constant.ACCESS_DENIED_PAGE;
    }
}
