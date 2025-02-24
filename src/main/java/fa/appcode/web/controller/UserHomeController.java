package fa.appcode.web.controller;

import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.City;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserHomeController {

    @GetMapping("/home")
    public String parentHome(Model model) {
        List<City> listCity = new ArrayList<>();
        return "user_side/index";
    }
    @GetMapping("/search")
    public String searchSchool(Model model) {
        return "user_side/search-school";
    }


}