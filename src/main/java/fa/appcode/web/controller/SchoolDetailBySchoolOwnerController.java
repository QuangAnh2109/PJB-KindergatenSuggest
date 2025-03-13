package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.utils.SchoolFormButton;
import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.entities.*;
import fa.appcode.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
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

    private final SchoolDetailManagerService schoolDetailManagerService;

    private final SchoolDetailOwnerService schoolDetailOwnerService;

    private final AccountService accountService;

    private final WardService wardService;

    private final DistrictService districtService;

    private final SchoolInfoService schoolInfoService;


    @GetMapping("/form")
    public String getSchoolForm(Model model) {
        return schoolDetailManagerService.getSchoolCreateFormToModel(model);
    }

    @ResponseBody
    @PostMapping("/save-draft-new")
    public ResponseEntity<?> saveDraft(
            @RequestParam("name") String name,
            @RequestParam("typeId") Integer typeKey,
            @RequestParam("cityId") Integer cityID,
            @RequestParam("districtId") Integer districtID,
            @RequestParam("wardId") Integer wardID,
            @RequestParam("address") String address,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("childReceivingAgeId") Integer ageTypeKey,
            @RequestParam("educationMethodId") Integer educationTypeKey,
            @RequestParam("feeFrom") BigDecimal feeFrom,
            @RequestParam("feeTo") BigDecimal feeTo,
            @RequestParam("introduction") String schoolIntroduction,
            @RequestParam("statusId") Integer statusId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "schoolFacilities",required = false) List<Integer> schoolFacilities,
            @RequestParam(value = "schoolUtilities",required = false) List<Integer> schoolUtilities,
            Principal principal
    ) {
        // Get current account
        AccountInfo currentAccount = accountService.getAccountInfo(principal);

        // Get location info
        Ward ward = wardService.findByIdAndNoDeleteFlg(wardID);
        District district = districtService.findByIdAndNoDeleteFlag(districtID);
        City city = cityService.findByIdAndNoDeleteFlg(cityID);

        // Create SchoolInfo object
        SchoolInfo schoolInfo = new SchoolInfo(
                currentAccount, name, email, image.toString(), phone, feeFrom, feeTo,
                address, ward, district, city, schoolIntroduction, Instant.now(),
                ageTypeKey, educationTypeKey, 1, statusId, 0,
                "SYSTEM_ADMIN", Instant.now(), " ", Instant.now(), false
        );

        // Save school
        schoolInfoService.createNewSchool(schoolInfo,image);

        //SchoolFacility schoolFacility = new SchoolFacility(schoolInfo,0,"test",Instant.now()," ",Instant.now(),false);
        return ResponseEntity.ok().body(Map.of("message", "School saved successfully!"));
    }

    @ResponseBody
    @GetMapping("/submit-new")
    public String addNewSchool(@RequestBody SchoolFormManager schoolFormManager) {
        System.out.println(schoolFormManager);
        return "ok";
    }

    @ResponseBody
    @GetMapping("/submit")
    public ResponseEntity submitSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
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
    @GetMapping("/update")
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

    @GetMapping("/view-detail")
    public String getSchoolDetail(@RequestParam("id") int id) {
        return "";
    }
}
