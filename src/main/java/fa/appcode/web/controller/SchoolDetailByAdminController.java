package fa.appcode.web.controller;

import fa.appcode.common.utils.*;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/school")
@RequiredArgsConstructor
public class SchoolDetailByAdminController {

    private final AccountService accountService;

    private final SchoolDetailManagerService schoolDetailManagerService;
    private final GlobalConfig globalConfig;

    @ResponseBody
    @PostMapping("/reject")
    public ResponseEntity<Map<String, Object>> rejectSchool(@RequestParam("schoolId") int id, @RequestParam("recordNo") int recordNo) {
        // Get school owner email
        String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);
        List<String> toEmail = email != null ? List.of(email) : null;

        // Send email to school owner
        Map<Placeholder, String> detail = Map.of(Placeholder.TITLE, "Admin reject School");
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_REJECTED, List.of(SchoolConstant.STATUS_SUBMITTED), MailConstant.MAIL_REJECT_SCHOOL, detail, toEmail, List.of());
    }


    @ResponseBody
    @PostMapping("/approve")
    public ResponseEntity<Map<String, Object>> approveSchool(@RequestParam("schoolId") int id, @RequestParam("recordNo") int recordNo) {
        // Get school owner email
        String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);
        List<String> toEmail = email != null ? List.of(email) : null;
        // Send email to school owner
        Map<Placeholder, String> detail = Map.of(Placeholder.TITLE, "Admin approve School", Placeholder.LINK, globalConfig.getServerLink() + Constant.VIEW_DETAIL_URL + id);
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_APPROVED, List.of(SchoolConstant.STATUS_SUBMITTED), MailConstant.MAIL_APPROVE_SCHOOL, detail, toEmail, List.of());
    }


}
