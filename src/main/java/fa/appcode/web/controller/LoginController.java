package fa.appcode.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
//    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$";
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(){
//            @RequestParam(value = "error", required = false) String error,
//            @RequestParam(value = "email", required = false) String email,
//            @RequestParam(value = "missing", required = false) String missing,
//            Model model) {

        return "user_side/login";
    }

}
