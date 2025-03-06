package fa.appcode.web.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class SchoolManagerController {

    @ResponseBody
    @PostMapping("/school/unpublish/{id}")
    public String unpublishSchoolByManager(@PathVariable("id") int id) {
        return "";
    }

    @ResponseBody
    @PostMapping("/school/publish/{id}")
    public String publishSchoolByManager(@PathVariable("id") int id) {
        return "";
    }
}
