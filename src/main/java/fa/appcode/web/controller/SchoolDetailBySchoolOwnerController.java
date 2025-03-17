package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.RoleConstant;
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

    private final DistrictService districtService;

    private final WardService wardService;

    private final AccountService accountService;

    private final SchoolDetailManagerService schoolDetailManagerService;

    private final SchoolDetailOwnerService schoolDetailOwnerService;

    @GetMapping("/form")
    public String getSchoolForm(Model model) {
        return schoolDetailManagerService.getSchoolCreateFormToModel(model);
    }

    @ResponseBody
    @GetMapping("/add-new")
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
                                       @RequestParam(value = "schoolFacilities",required = false) List<Integer> schoolFacilities,
                                       @RequestParam(value = "schoolUtilities",required = false) List<Integer> schoolUtilities,
                                       Principal principal) {
        // Get current account
        AccountInfo currentAccount = accountService.getAccountInfo(principal);

//        // Get location info
//        Ward ward = wardService.findByIdAndNoDeleteFlg(wardID);
//        District district = districtService.findByIdAndNoDeleteFlag(districtID);
//        City city = cityService.findByIdAndNoDeleteFlg(cityID);

        // Create SchoolInfo object
        SchoolInfo schoolInfo = new SchoolInfo(
                currentAccount, name, email, image.toString(), phone, feeFrom, feeTo,
                address, null, null, null, schoolIntroduction, Instant.now(),
                ageTypeKey, educationTypeKey, 1, statusId, 0,
                RoleConstant.SCHOOL_OWNER, Instant.now(), RoleConstant.SCHOOL_OWNER, Instant.now(), false
        );

        return schoolDetailManagerService.createNewSchool(schoolInfo, image, schoolFacilities, schoolUtilities);
    }
}
