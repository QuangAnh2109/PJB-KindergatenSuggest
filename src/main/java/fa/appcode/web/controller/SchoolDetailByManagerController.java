package fa.appcode.web.controller;

import fa.appcode.common.utils.MailConstant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.RoleConstant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.City;
import fa.appcode.entities.District;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.entities.Ward;
import fa.appcode.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.Role;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manager/school")
@RequiredArgsConstructor
public class SchoolDetailByManagerController {
    private final SchoolDetailManagerService schoolDetailManagerService;

    private final SchoolInfoService schoolInfoService;

    private final GlobalConfig globalConfig;

    private final AccountService accountService;

    private final CityService cityService;

    private final DistrictService districtService;

    private final WardService wardService;

    @GetMapping("/view-detail")
    public String getSchoolDetail(@RequestParam("id") int id, Model model) {
        return schoolDetailManagerService.getSchoolDetail(model, id);
    }

    @ResponseBody
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteSchool(@RequestParam("schoolId") int id, @RequestParam("recordNo") int recordNo) {
        List<Integer> inStatus = List.of(SchoolConstant.STATUS_SAVED, SchoolConstant.STATUS_SUBMITTED, SchoolConstant.STATUS_APPROVED, SchoolConstant.STATUS_REJECTED, SchoolConstant.STATUS_PUBLISHED, SchoolConstant.STATUS_UNPUBLISHED);
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_DELETED, inStatus, null, null, null, null);
    }

    @ResponseBody
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateSchool(
            @RequestParam("id") int id,
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
            @RequestParam(value = "schoolFacilities",required = false) List<Integer> schoolFacilityID,
            @RequestParam(value = "schoolUtilities",required = false) List<Integer> schoolUtilityID,
            @RequestParam("recordNo") int recordNo) {

        String updateId = RoleConstant.SCHOOL_OWNER;
        for(GrantedAuthority grantedAuthority:SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
            if(grantedAuthority.getAuthority().equals("Admin")){
                updateId = RoleConstant.ADMIN;
                break;
            }
        }

        // Create SchoolInfo object
        SchoolFormManager schoolInfo = new SchoolFormManager(
                false, null, recordNo, id,
                typeKey, name, address, cityID, districtID, wardID,
                email, phone, ageTypeKey, educationTypeKey, feeTo,
                feeFrom, schoolIntroduction, null, Instant.now(), updateId
        );

        return schoolDetailManagerService.updateSchool(schoolInfo, image, schoolFacilityID, schoolUtilityID);
    }

    @ResponseBody
    @PostMapping("/public")
    public ResponseEntity<Map<String, Object>> publicSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
        // Get school owner email
        String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

        List<Integer> inStatus = List.of(SchoolConstant.STATUS_APPROVED, SchoolConstant.STATUS_UNPUBLISHED);
        Map<Placeholder, String> detail = Map.of(Placeholder.TITLE, "Admin Public School", Placeholder.SCHOOL_NAME, schoolInfoService.getSchoolNameBySchoolIdAndNoDelete(id), Placeholder.USER_NAME, accountService.getAccountNameByEmailAndNoDelete(SecurityContextHolder.getContext().getAuthentication().getName()), Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id);
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_PUBLISHED, inStatus, MailConstant.MAIL_PUBLISH_SCHOOL, detail, List.of(email), List.of());
    }

    @ResponseBody
    @PostMapping("/unpublic")
    public ResponseEntity<Map<String, Object>> unpublicSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_UNPUBLISHED, List.of(SchoolConstant.STATUS_PUBLISHED), null, null, null, null);
    }
}
