package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MySchoolController {
    @GetMapping("/parent/my-school")
    public String mySchool() {
        return "user_side/my-school";
    }
}
