package fa.appcode.web.controller;

import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.CityService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolInfoService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fa.appcode.common.utils.Constant.EMAIL_REGEX_HTML;
import static fa.appcode.common.utils.Constant.PHONE_REGEX_HTML;

@Controller
@RequestMapping("/school-owner")
@AllArgsConstructor
public class SchoolSchoolOwnerController {
    private final MasterDatumService masterDatumService;

    private final CityService cityService;

    private final SchoolInfoService schoolInfoService;

    private final AccountService accountService;

    @GetMapping("/school/form")
    public String getSchoolFormBySchoolOwner(Model model) {
        // Get all master data by type name(SCHOOL TYPE, CHILD RECEIVING AGE, EDUCATION METHOD, FACILITIES, UTILITIES) in no delete
        List<MasterDataVo> masterDataVoList = masterDatumService.findAllByTypeNameInNoDelete(List.of("SCHOOL TYPE", "CHILD RECEIVING AGE", "EDUCATION METHOD", "FACILITIES", "UTILITIES"));

        // Create list to store each type of master data
        List<MasterDataVo> schoolTypes = new ArrayList<>(), childReceivingAges = new ArrayList<>(), educationMethods = new ArrayList<>(), facilities = new ArrayList<>(), utilities = new ArrayList<>();
        for (MasterDataVo vo : masterDataVoList) {
            switch (vo.getTypeName()) {
                case "SCHOOL TYPE":
                    schoolTypes.add(vo);
                    break;
                case "CHILD RECEIVING AGE":
                    childReceivingAges.add(vo);
                    break;
                case "EDUCATION METHOD":
                    educationMethods.add(vo);
                    break;
                case "FACILITIES":
                    facilities.add(vo);
                    break;
                case "UTILITIES":
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

        return "admin_side/school-form";
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
        try{
            // Find school info by id and account id
            SchoolInfo schoolInfo = schoolInfoService.findSchoolInfoByIdAndAccountIdNoDelete(id, getAuthenAccountId());
            if(schoolInfo != null) {
                throw new Exception("School not found");
            }
            if(schoolInfo.getStatusId() != 1) {
                throw new Exception("School not in saved status");
            }

            // Set status to submit
            schoolInfo.setStatusId(2);

            // Increase record no
            schoolInfo.setRecordNo(schoolInfo.getRecordNo() + 1);
            schoolInfo.setUpdateId("USER_ADMIN");
            schoolInfo.setUpdateTime(Instant.now());

            // Save school info
            schoolInfoService.save(schoolInfo);

            return ResponseEntity.accepted().body("Submit success");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Submit failed: " + e.getMessage());
        }
    }

    @ResponseBody
    @PatchMapping("/school/delete/{id}")
    public ResponseEntity deleteSchoolBySchoolOwner(@PathVariable("id") int id) {
        try{
            // Find school info by id and account id
            SchoolInfo schoolInfo = schoolInfoService.findSchoolInfoByIdAndAccountIdNoDelete(id, getAuthenAccountId());
            if(schoolInfo != null) {
                throw new Exception("School not found");
            }
            if(schoolInfo.getStatusId() == 3) {
                throw new Exception("Don't have permission to delete");
            }
            if(schoolInfo.getStatusId() == 7) {
                throw new Exception("School already deleted");
            }

            // Set status to delete
            schoolInfo.setStatusId(7);

            // Increase record no
            schoolInfo.setRecordNo(schoolInfo.getRecordNo() + 1);
            schoolInfo.setUpdateId("USER_ADMIN");
            schoolInfo.setUpdateTime(Instant.now());

            // Save school info
            schoolInfoService.save(schoolInfo);

            return ResponseEntity.accepted().body("Delete success");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Delete failed " + e.getMessage());
        }
    }

    @GetMapping("/school/detail/{id}")
    public String getSchoolDetailBySchoolOwner(@PathVariable("id") int id) {
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

    // Get account id from authentication
    private int getAuthenAccountId() throws Exception {
        return accountService.findAccountByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
    }
}
