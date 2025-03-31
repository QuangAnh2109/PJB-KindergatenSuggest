package fa.appcode.web.controller;

import fa.appcode.common.utils.*;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.*;
import fa.appcode.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/school-owner/school")
@RequiredArgsConstructor
public class SchoolDetailBySchoolOwnerController {
    private final MasterDatumService masterDatumService;

    private final CityService cityService;

    private final DistrictService districtService;

    private final WardService wardService;

    private final AccountService accountService;

    private final SchoolDetailManagerService schoolDetailManagerService;

    private final GlobalConfig globalConfig;

    private final EntityValidateService entityValidateService;

    @GetMapping("/form")
    public String getSchoolForm(Model model) {
        return schoolDetailManagerService.getSchoolCreateFormToModel(model);
    }

    @ResponseBody
    @PostMapping("/add-new")
    public ResponseEntity addNewSchool(@RequestParam("name") String name,
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
                                       @RequestParam(value = "schoolFacilities",required = false) List<Integer> schoolFacilityID,
                                       @RequestParam(value = "schoolUtilities",required = false) List<Integer> schoolUtilityID,
                                       Principal principal) {
        // Get current account
        AccountInfo currentAccount = accountService.getAccountInfo(principal);

        // Get location info
        Ward ward = wardService.findByIdAndNoDeleteFlg(wardID);
        District district = districtService.findByIdAndNoDeleteFlag(districtID);
        City city = cityService.findByIdAndNoDeleteFlg(cityID);

        // Create SchoolInfo object
        SchoolInfo schoolInfo = new SchoolInfo(
                currentAccount, name, email, null, phone, feeFrom, feeTo,
                address, ward, district, city, schoolIntroduction, Instant.now(),
                ageTypeKey, educationTypeKey, typeKey, statusId, 0,
                RoleConstant.SCHOOL_OWNER, Instant.now(), RoleConstant.SCHOOL_OWNER, Instant.now(), false
        );

        entityValidateService.validateUpdateSchool(schoolInfo, image);

        return schoolDetailManagerService.createNewSchool(schoolInfo, image, schoolFacilityID, schoolUtilityID);
    }

    @ResponseBody
    @PostMapping("/submit")
    public ResponseEntity<Map<String,Object>> submitSchool(@RequestParam("schoolId") int id, @RequestParam("recordNo") int recordNo) {
        List<Integer> inStatus = List.of(SchoolConstant.STATUS_SAVED);
        Map<Placeholder, String> detail = Map.of(Placeholder.TITLE, "waiting for approve", Placeholder.LINK,  globalConfig.getServerLink()+ "/manager/school/view-detail?id=" + id);
        List<String> toMail = accountService.getAllAccountEmailsByRole(RoleConstant.ADMIN_ROLE.getKey());
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_SUBMITTED, inStatus, MailConstant.MAIL_SUBMIT_SCHOOL, detail, toMail, List.of());
    }
}
