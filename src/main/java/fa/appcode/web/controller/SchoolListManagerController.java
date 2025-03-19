package fa.appcode.web.controller;

import fa.appcode.services.SchoolListManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class SchoolListManagerController {

    private final SchoolListManagerService schoolListManagerService;

    @GetMapping("/school-list")
    public String getSchoolListManager(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "ajax", required = false, defaultValue = "0") boolean ajax, Model model) {
        return schoolListManagerService.setSchoolDataToModelBySearchAndPage(search, page, ajax, model);
    }
}