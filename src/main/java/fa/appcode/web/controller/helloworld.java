package fa.appcode.web.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class helloworld {
    @GetMapping("/")
    public String hello(Model model) {
        model.addAttribute("message", "Hello, Spring Boot with JSP!");
        return "hello";
    }
}
