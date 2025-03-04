package fa.appcode.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/manager")
@AllArgsConstructor
public class SchoolManagerController {

    @PostMapping("/school/unpublish/{id}")
    public String unpublishSchoolByManager(@PathVariable("id") int id) {
        return "";
    }

    @PostMapping("/school/publish/{id}")
    public String publishSchoolByManager(@PathVariable("id") int id) {
        return "";
    }
}
