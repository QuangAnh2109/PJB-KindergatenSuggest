package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import fa.appcode.services.SchoolInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class SchoolAdminController {

    private final SchoolInfoService schoolInfoService;

    private final EmailService emailService;

    private final AccountService accountService;

    private final GlobalConfig globalConfig;

    private static final int APPROVE_MAIL_ID = 4;

    @ResponseBody
    @PostMapping("/school/delete/{id}&{recordNo}")
    public ResponseEntity deleteSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, Constant.SCHOOL_STATUS_DELETED_ID, Constant.ADMIN, List.of()) > 0) {
            return ResponseEntity.accepted().body("Delete success");
        } else return ResponseEntity.badRequest().body("Delete failed");
    }

    @GetMapping("/school/detail/{id}&{edit}")
    public String getSchoolDetailByAdmin(@PathVariable("id") int id, @PathVariable("edit") boolean edit, Model model) {
        model.addAttribute("school", schoolInfoService.getSchoolFormByIdAndNoDelete(id));
        model.addAttribute("edit", edit);
        return "";
    }

    @ResponseBody
    @PostMapping("/school/update")
    public String updateSchoolByAdmin(@RequestBody Map<String, Object> map) {

        return "";
    }

    @ResponseBody
    @PostMapping("/school/submit/{id}&{recordNo}")
    public ResponseEntity submitSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        List<Integer> status = List.of(Constant.SCHOOL_STATUS_SUBMITTED_ID, Constant.SCHOOL_STATUS_REJECTED_ID, Constant.SCHOOL_STATUS_PUBLISHED_ID, Constant.SCHOOL_STATUS_UNPUBLISHED_ID, Constant.SCHOOL_STATUS_DELETED_ID);
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, Constant.SCHOOL_STATUS_APPROVED_ID, Constant.ADMIN, status) > 0) {
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);
            Map<Placeholder, String> detail = Map.of(Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id, Placeholder.TITLE, "Admin approve school");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(APPROVE_MAIL_ID).detail(detail).build());
            return ResponseEntity.accepted().body("Submit success");
        } else return ResponseEntity.badRequest().body("Submit failed");
    }

    @ResponseBody
    @PostMapping("/school/reject/{id}&{recordNo}")
    public ResponseEntity rejectSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        List<Integer> status = List.of(Constant.SCHOOL_STATUS_SAVED_ID, Constant.SCHOOL_STATUS_APPROVED_ID, Constant.SCHOOL_STATUS_PUBLISHED_ID, Constant.SCHOOL_STATUS_UNPUBLISHED_ID, Constant.SCHOOL_STATUS_DELETED_ID);
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, Constant.SCHOOL_STATUS_REJECTED_ID, Constant.ADMIN, List.of()) > 0) {
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);
            Map<Placeholder, String> detail = Map.of(Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id, Placeholder.TITLE, "Admin approve school");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(APPROVE_MAIL_ID).detail(detail).build());
            return ResponseEntity.accepted().body("Reject success");
        } else return ResponseEntity.badRequest().body("Reject failed");
    }


    @ResponseBody
    @PostMapping("/school/approve/{id}&{recordNo}")
    public ResponseEntity approveSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        List<Integer> status = List.of(Constant.SCHOOL_STATUS_SAVED_ID, Constant.SCHOOL_STATUS_REJECTED_ID, Constant.SCHOOL_STATUS_PUBLISHED_ID, Constant.SCHOOL_STATUS_UNPUBLISHED_ID, Constant.SCHOOL_STATUS_DELETED_ID);
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, Constant.SCHOOL_STATUS_APPROVED_ID, Constant.ADMIN, List.of()) > 0) {
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);
            Map<Placeholder, String> detail = Map.of(Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id, Placeholder.TITLE, "Admin approve school");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(APPROVE_MAIL_ID).detail(detail).build());
            return ResponseEntity.accepted().body("Approve success");
        } else return ResponseEntity.badRequest().body("Approve failed");
    }
}