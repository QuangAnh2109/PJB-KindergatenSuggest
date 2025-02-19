package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SchoolFormController {
    @GetMapping("/admin/school-form")
    public String home() {
        return "admin_side/add-new-school-form";
    }
}
