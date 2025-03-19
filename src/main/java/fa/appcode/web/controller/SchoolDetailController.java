package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@RequestMapping("/public/school")
@Controller
public class SchoolDetailController {
    @GetMapping("/details")
    public String schoolDetail(Model model) {
        return "user_side/school-single";
    }

}
