package fa.appcode.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SchoolFormController {
    @GetMapping("/admin/school-form")
    public String home(HttpServletRequest res) {
        res.setAttribute("schoolIntroduction", "<p>Hello World!</p>\n<p>Some initial <strong>bold</strong> text</p>\n<p><br /></p>");
        return "school-form";
    }
}
