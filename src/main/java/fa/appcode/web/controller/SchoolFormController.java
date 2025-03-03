package fa.appcode.web.controller;

import fa.appcode.services.CityService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.impl.CityServiceImpl;
import fa.appcode.services.impl.MasterDatumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static fa.appcode.common.utils.Constant.EMAIL_REGEX_HTML;
import static fa.appcode.common.utils.Constant.PHONE_REGEX_HTML;

@Controller
@RequestMapping("/admin")
public class SchoolFormController {
    @Autowired
    private MasterDatumService masterDatumService;

    @Autowired
    private CityService cityService;

    @GetMapping("/school-form")
    public String schoolForm(Model model) {
        model.addAttribute("schoolTypes", masterDatumService.findAllByTypeNameNoDelete("SCHOOL TYPE"));
        model.addAttribute("childReceivingAges", masterDatumService.findAllByTypeNameNoDelete("CHILD RECEIVING AGE"));
        model.addAttribute("educationMethods", masterDatumService.findAllByTypeNameNoDelete("EDUCATION METHOD"));
        model.addAttribute("facilities", masterDatumService.findAllByTypeNameNoDelete("FACILITIES"));
        model.addAttribute("utilities", masterDatumService.findAllByTypeNameNoDelete("UTILITIES"));
        model.addAttribute("emailRegex", EMAIL_REGEX_HTML);
        model.addAttribute("phoneRegex", PHONE_REGEX_HTML);
        model.addAttribute("citys", cityService.findAllByNoDelete());
        return "admin_side/school-manager-detail";
    }

    @PostMapping("/school-form/submit")
    public String schoolSubmit(Model model) {
        return "admin_side/school-manager-detail";
    }

    @PostMapping("/school-form/save-draft")
    public String schoolSaveDraft(Model model) {
        return "admin_side/school-manager-detail";
    }

    @GetMapping("/school-form/{id}")
    public String schoolDetail(@PathVariable("id") int id) {
        return "admin_side/school-manager-detail";
    }
}
