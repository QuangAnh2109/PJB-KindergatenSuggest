package fa.appcode.web.controller;


import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.MySchoolVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.EnrollSchoolService;
import fa.appcode.services.impl.EnrollSchoolServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
public class MySchoolController {

    private final GlobalConfig globalConfig;
    private final EnrollSchoolService enrollSchoolService;
    private final EnrollSchoolServiceImpl enrollSchoolServiceImpl;

    @GetMapping("/parent/my-school")
    public String mySchool(@SessionAttribute(name = "idAccount", required = true) Integer id,
                           Model model,
                           @RequestParam(defaultValue = Constant.INIT_PAGE) int currentPage,
                           @RequestParam(defaultValue = "enrolled") String activeTab) {
        Pageable pageable = PageRequest.of(currentPage,globalConfig.getSizeOfPage());
        Page<MySchoolVo> listSchoolEnrollByParentId = enrollSchoolService.findListSchoolParentEnrolledByParentId(id,pageable);
        Page<MySchoolVo> listSchoolPreEnrollByParentId = enrollSchoolService.findListSchoolParentPreEnrolledByParentId(id,pageable);
        Map<Integer,List<String>> listFacilitiesOfCurrentSchool = enrollSchoolServiceImpl.getFacilitiesMapForSchools(listSchoolEnrollByParentId);
        Map<Integer,List<String>> listFacilitiesOfPreSchool = enrollSchoolServiceImpl.getFacilitiesMapForSchools(listSchoolPreEnrollByParentId);


        //Response data
        model.addAttribute("facilitiesMapCurrentSchool",listFacilitiesOfCurrentSchool);
        model.addAttribute("facilitiesMapPreSchool",listFacilitiesOfPreSchool);
        model.addAttribute("listSchoolPreEnroll", listSchoolPreEnrollByParentId);
        model.addAttribute("listSchoolEnroll", listSchoolEnrollByParentId);
        return "user_side/my-school";
    }
}
