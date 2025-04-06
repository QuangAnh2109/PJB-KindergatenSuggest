package fa.appcode.web.controller;

import fa.appcode.common.vo.*;
import fa.appcode.repositories.SchoolFacilityRepository;
import fa.appcode.services.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/public")
@Controller
@AllArgsConstructor
public class SchoolDetailController {

    private final FeedbackService feedbackService;
    private final SchoolInfoService schoolInfoService;
    private final MasterDatumService masterDatumService;
    private final SchoolFacilityService schoolFacilityService;
    private final SchoolUtilityService schoolUtilityService;
    private final EnrollSchoolService enrollSchoolService;


    @GetMapping("/school/details/{schoolId}")
    public String schoolDetail(@SessionAttribute(name = "idAccount",required = false)Integer accountId, @PathVariable("schoolId") Integer schoolId, Model model) {

        RatingVo ratingOfSchool = feedbackService.findRatingBySchoolId(schoolId);
        MySchoolVo schoolInfo = schoolInfoService.findSchoolDetailBySchoolId(schoolId);
        List<MasterDataVo> listFacilities = masterDatumService.findAllByTypeNameNoDelete("FACILITIES");
        List<MasterDataVo> listUtilities = masterDatumService.findAllByTypeNameNoDelete("UTILITIES");
        Set<Integer> facilitiesId = new HashSet<>(schoolFacilityService.getAllSchoolFacilityIdBySchoolIdAndNoDeleteFlg(schoolId));
        Set<Integer> utilitiesId = new HashSet<>(schoolUtilityService.getAllSchoolUtilityIdBySchoolIdAndNoDelete(schoolId));
        RatingVo ratingSchool = feedbackService.findRatingBySchoolId(schoolId);
        List<FeedbackListVo> listFeedback = feedbackService.findListFeedbackBySchoolId(schoolId);


        if(accountId != null) {
            Boolean isEnroll = enrollSchoolService.isEnrolled(accountId, schoolId);
            model.addAttribute("isEnroll", isEnroll);
        }

        model.addAttribute("facilitiesId", facilitiesId);
        model.addAttribute("ratingOfSchool", ratingSchool);
        model.addAttribute("utilitiesId", utilitiesId);
        model.addAttribute("facilities", listFacilities);
        model.addAttribute("utilities", listUtilities);
        model.addAttribute("schoolInfo", schoolInfo);
        model.addAttribute("rating", ratingOfSchool);
        model.addAttribute("listFeedback", listFeedback);
        return "user_side/school-single";
    }
}
