package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.utils.SchoolFormButton;
import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.CityService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fa.appcode.common.utils.Constant.EMAIL_REGEX_HTML;
import static fa.appcode.common.utils.Constant.PHONE_REGEX_HTML;

@Controller
@RequestMapping("/school-owner/school")
@RequiredArgsConstructor
public class SchoolDetailBySchoolOwnerController {
    private final MasterDatumService masterDatumService;

    private final CityService cityService;

    @GetMapping("/form")
    public String getSchoolForm(Model model) {
        // Get all master data by type name(SCHOOL TYPE, CHILD RECEIVING AGE, EDUCATION METHOD, FACILITIES, UTILITIES) in no delete
        List<MasterDataVo> masterDataVoList = masterDatumService.findAllByTypeNameInNoDelete(List.of(SchoolConstant.SCHOOL_TYPE, SchoolConstant.CHILD_RECEIVING_AGE, SchoolConstant.EDUCATION_METHOD, SchoolConstant.FACILITIES, SchoolConstant.UTILITIES));

        // Create list to store each type of master data
        List<MasterDataVo> schoolTypes = new ArrayList<>(), childReceivingAges = new ArrayList<>(), educationMethods = new ArrayList<>(), facilities = new ArrayList<>(), utilities = new ArrayList<>();
        for (MasterDataVo vo : masterDataVoList) {
            switch (vo.getTypeName()) {
                case SchoolConstant.SCHOOL_TYPE:
                    schoolTypes.add(vo);
                    break;
                case SchoolConstant.CHILD_RECEIVING_AGE:
                    childReceivingAges.add(vo);
                    break;
                case SchoolConstant.EDUCATION_METHOD:
                    educationMethods.add(vo);
                    break;
                case SchoolConstant.FACILITIES:
                    facilities.add(vo);
                    break;
                case SchoolConstant.UTILITIES:
                    utilities.add(vo);
                    break;
            }
        }

        // Add all master data to model
        model.addAttribute("schoolTypes", schoolTypes);
        model.addAttribute("childReceivingAges", childReceivingAges);
        model.addAttribute("educationMethods", educationMethods);
        model.addAttribute("facilities", facilities);
        model.addAttribute("utilities", utilities);

        // Add regex to model
        model.addAttribute("emailRegex", EMAIL_REGEX_HTML);
        model.addAttribute("phoneRegex", PHONE_REGEX_HTML);

        // Add city list to model
        model.addAttribute("citys", cityService.findAllByNoDelete());

        SchoolFormButton.valueOf("owner1").getSchoolFormButtonBuild().setButton(model);

        model.addAttribute("edit", true);

        return Constant.SCHOOL_CREATE_PAGE;
    }

    @ResponseBody
    @PostMapping("/save-draft-new")
    public String saveDraft(@Valid @RequestBody SchoolFormManager schoolFormManager) {
        return "";
    }

    @ResponseBody
    @PostMapping("/submit-new")
    public String addNewSchool(@Valid @RequestBody SchoolFormManager schoolFormManager) {
        return "";
    }

    @ResponseBody
    @PatchMapping("/submit")
    public ResponseEntity submitSchool(@RequestParam("id") int id) {
        try {
//            String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//            // Find school info by id and account id
//            SchoolInfo schoolInfo = schoolInfoService.findSchoolInfoByIdAndAccountNoDelete(id, email);
//
//            // Check school info
//            if (schoolInfo == null) {
//                throw new Exception("School not found");
//            }
//            else if (schoolInfo.getStatusId() != Constant.SCHOOL_STATUS_SAVED_ID) {
//                throw new Exception("School not in saved status");
//            }
//
//            // Set status to submit
//            schoolInfo.setStatusId(Constant.SCHOOL_STATUS_SUBMITTED_ID);
//
//            // Increase record no
//            schoolInfo.setRecordNo(schoolInfo.getRecordNo() + 1);
//            schoolInfo.setUpdateId(Constant.SCHOOL_OWNER);
//            schoolInfo.setUpdateTime(Instant.now());
//
//            // Save school info
//            //schoolInfoService.save(schoolInfo);

            return ResponseEntity.accepted().body("Submit success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Submit failed: " + e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("/delete")
    public String deleteSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
        return "";
    }

    @ResponseBody
    @PatchMapping("/update")
    public String updateSchool(@Valid @RequestBody SchoolFormManager schoolFormManager) {
        return "";
    }

    @ResponseBody
    @GetMapping("/unpublish")
    public String unpublishSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
        //SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[Admin]")
        return "";
    }

    @ResponseBody
    @GetMapping("/publish")
    public String publishSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
        return "";
    }

    @ResponseBody
    @GetMapping("/submit")
    public String submitSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
        return "";
    }

    @GetMapping("/view-detail")
    public String getSchoolDetail(@RequestParam("id") int id) {
        return "";
    }
}
