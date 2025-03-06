package fa.appcode.web.controller;

import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.CityService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static fa.appcode.common.utils.Constant.EMAIL_REGEX_HTML;
import static fa.appcode.common.utils.Constant.PHONE_REGEX_HTML;

@Controller
@RequestMapping("/school-owner")
@RequiredArgsConstructor
public class SchoolSchoolOwnerController {
    private final MasterDatumService masterDatumService;

    private final CityService cityService;

    private final SchoolInfoService schoolInfoService;

    private final AccountService accountService;

    private final String SCHOOL_TYPE = "SCHOOL TYPE";

    private final String CHILD_RECEIVING_AGE = "CHILD RECEIVING AGE";

    private final String EDUCATION_METHOD = "EDUCATION METHOD";

    private final String FACILITIES = "FACILITIES";

    private final String UTILITIES = "UTILITIES";

    private final String SCHOOL_FORM_HTML = "admin_side/school-form";

    @GetMapping("/school/form")
    public String getSchoolFormBySchoolOwner(Model model) {
        // Get all master data by type name(SCHOOL TYPE, CHILD RECEIVING AGE, EDUCATION METHOD, FACILITIES, UTILITIES) in no delete
        List<MasterDataVo> masterDataVoList = masterDatumService.findAllByTypeNameInNoDelete(List.of(SCHOOL_TYPE, CHILD_RECEIVING_AGE, EDUCATION_METHOD, FACILITIES, UTILITIES));

        // Create list to store each type of master data
        List<MasterDataVo> schoolTypes = new ArrayList<>(), childReceivingAges = new ArrayList<>(), educationMethods = new ArrayList<>(), facilities = new ArrayList<>(), utilities = new ArrayList<>();
        for (MasterDataVo vo : masterDataVoList) {
            switch (vo.getTypeName()) {
                case SCHOOL_TYPE:
                    schoolTypes.add(vo);
                    break;
                case CHILD_RECEIVING_AGE:
                    childReceivingAges.add(vo);
                    break;
                case EDUCATION_METHOD:
                    educationMethods.add(vo);
                    break;
                case FACILITIES:
                    facilities.add(vo);
                    break;
                case UTILITIES:
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

        return SCHOOL_FORM_HTML;
    }

    @ResponseBody
    @PostMapping("/school/submit")
    public String addSchoolBySchoolOwner() {
        SchoolInfo schoolInfo = new SchoolInfo();
        return "";
    }

    @ResponseBody
    @PatchMapping("/school/submit/{id}")
    public ResponseEntity addSchoolBySchoolOwner(@PathVariable("id") int id) {
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
    @PatchMapping("/school/delete/{id}")
    public ResponseEntity deleteSchoolBySchoolOwner(@PathVariable("id") int id) {
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
//            else if (schoolInfo.getStatusId() == Constant.SCHOOL_STATUS_APPROVED_ID) {
//                throw new Exception("Don't have permission to delete");
//            }
//            else if (schoolInfo.getStatusId() == Constant.SCHOOL_STATUS_DELETED_ID) {
//                throw new Exception("School already deleted");
//            }
//
//            // Set status to delete
//            schoolInfo.setStatusId(Constant.SCHOOL_STATUS_DELETED_ID);
//
//            // Increase record no
//            schoolInfo.setRecordNo(schoolInfo.getRecordNo() + 1);
//            schoolInfo.setUpdateId(Constant.SCHOOL_OWNER);
//            schoolInfo.setUpdateTime(Instant.now());
//
//            // Save school info
//            //schoolInfoService.save(schoolInfo);

            return ResponseEntity.accepted().body("Delete success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Delete failed " + e.getMessage());
        }
    }

    @GetMapping("/school/detail/{id}")
    public String getSchoolDetailBySchoolOwner(@PathVariable("id") int id) {
        return "";
    }

    @GetMapping("/school/edit/{id}")
    public String getSchoolDetailForEditBySchoolOwner(@PathVariable("id") int id) {
        return "";
    }

    @ResponseBody
    @PatchMapping("/school/update/{id}")
    public String updateSchoolBySchoolOwner(@PathVariable("id") int id) {
        return "";
    }

    @ResponseBody
    @PostMapping("/school/save-draft")
    public String saveDraftBySchoolOwner() {
        return "";
    }
}
