package fa.appcode.web.controller;

import fa.appcode.services.CityService;
import fa.appcode.services.impl.CityServiceImpl;
import fa.appcode.services.impl.MasterDatumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class SchoolFormController {
    @Autowired
    private MasterDatumServiceImpl masterDatumServiceImpl;

    @Autowired
    private CityServiceImpl cityServiceImpl;

    @GetMapping("/admin/school-form")
    public String schoolForm(Model model) {
        String EMAIL_REGEX_HTML = "\\w[\\w0-9]*@gmail.com", PHONE_REGEX_HTML = "/(84[3|5|7|8|9])+([0-9]{8})\\b/g";
        model.addAttribute("schoolTypes",masterDatumServiceImpl.findAllByTypeNameNoDelete("SCHOOL TYPE"));
        model.addAttribute("childReceivingAges",masterDatumServiceImpl.findAllByTypeNameNoDelete("CHILD RECEIVING AGE"));
        model.addAttribute("educationMethods",masterDatumServiceImpl.findAllByTypeNameNoDelete("EDUCATION METHOD"));
        model.addAttribute("facilities",masterDatumServiceImpl.findAllByTypeNameNoDelete("FACILITIES"));
        model.addAttribute("utilities",masterDatumServiceImpl.findAllByTypeNameNoDelete("UTILITIES"));
        model.addAttribute("emailRegex", EMAIL_REGEX_HTML);
        model.addAttribute("phoneRegex", PHONE_REGEX_HTML);
        model.addAttribute("citys",cityServiceImpl.findAllByDeleteFlg(false));
        return "admin_side/school-manager-detail";
    }

    @PostMapping("/admin/school-form/submit")
    public String schoolSubmit(Model model) {
        return "admin_side/school-manager-detail";
    }

    @PostMapping("/admin/school-form/save-draft")
    public String schoolSaveDraft(Model model) {
        return "admin_side/school-manager-detail";
    }

    @GetMapping("admin/school-form/{id}")
    public String schoolDetail(@PathVariable("id") int id){
        return "admin_side/school-manager-detail";
    }
}
