package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import fa.appcode.services.SchoolInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    private final int APPROVE_MAIL_ID = 4;

    private final int REJECT_MAIL_ID = 5;

    @ResponseBody
    @PostMapping("/school/delete/{id}&{recordNo}")
    public ResponseEntity deleteSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        // Check school status is submitted, approved, rejected, published, unpublished
        List<Integer> inStatus = List.of(Constant.SCHOOL_STATUS_SUBMITTED_ID, Constant.SCHOOL_STATUS_APPROVED_ID, Constant.SCHOOL_STATUS_REJECTED_ID, Constant.SCHOOL_STATUS_PUBLISHED_ID, Constant.SCHOOL_STATUS_UNPUBLISHED_ID);

        // Update school status to deleted
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, Constant.SCHOOL_STATUS_DELETED_ID, Constant.ADMIN, inStatus) > 0) {
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
    public ResponseEntity updateSchoolByAdmin(@Valid @ModelAttribute SchoolFormManager schoolFormManager) {
        // Get school info by id
        SchoolFormManager schoolInfo = schoolInfoService.getSchoolFormByIdAndNoDelete(schoolFormManager.getId());

        // Check schoolFormManager actually changed and same record
        if (!schoolInfo.equals(schoolFormManager) && schoolInfo.getRecordNo() == schoolFormManager.getRecordNo()) {

            // Set school status to approved
            if(!List.of(Constant.SCHOOL_STATUS_DELETED_ID, Constant.SCHOOL_STATUS_REJECTED_ID).contains(schoolInfo.getStatusId())) {
                schoolFormManager.setStatusId(Constant.SCHOOL_STATUS_APPROVED_ID);
            }

            // Set user is admin
            schoolFormManager.setSchoolOwnerEmail(null);

            // Update school info
            if (schoolInfoService.updateSchoolInfoBySchoolFormManager(schoolFormManager) > 0) {
                return ResponseEntity.accepted().body("Update success");
            }
        }
        return ResponseEntity.badRequest().body("Update failed");
    }

    @ResponseBody
    @PostMapping("/school/submit/{id}&{recordNo}")
    public ResponseEntity submitSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        // Check school status is saved or submitted
        List<Integer> inStatus = List.of(Constant.SCHOOL_STATUS_SAVED_ID, Constant.SCHOOL_STATUS_SUBMITTED_ID);

        // Update school status
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, Constant.SCHOOL_STATUS_APPROVED_ID, Constant.ADMIN, inStatus) > 0) {

            // Get school owner email
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

            // Send email to school owner
            Map<Placeholder, String> detail = Map.of(Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id + "&0", Placeholder.TITLE, "Admin Submit and Approve School");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(APPROVE_MAIL_ID).detail(detail).build());

            return ResponseEntity.accepted().body("Submit success");
        } else return ResponseEntity.badRequest().body("Submit failed");
    }

    @ResponseBody
    @PostMapping("/school/reject/{id}&{recordNo}")
    public ResponseEntity rejectSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        // Check school status is submitted
        List<Integer> inStatus = List.of(Constant.SCHOOL_STATUS_SUBMITTED_ID);

        // Update school status to rejected
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, Constant.SCHOOL_STATUS_REJECTED_ID, Constant.ADMIN, inStatus) > 0) {
            // Get school owner email
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

            // Send email to school owner
            Map<Placeholder, String> detail = Map.of(Placeholder.TITLE, "Admin Reject School");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(REJECT_MAIL_ID).detail(detail).build());

            return ResponseEntity.accepted().body("Reject success");
        } else return ResponseEntity.badRequest().body("Reject failed");
    }


    @ResponseBody
    @PostMapping("/school/approve/{id}&{recordNo}")
    public ResponseEntity approveSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        // Check school status is submitted
        List<Integer> inStatus = List.of(Constant.SCHOOL_STATUS_SUBMITTED_ID);

        // Update school status to approved
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, Constant.SCHOOL_STATUS_APPROVED_ID, Constant.ADMIN, inStatus) > 0) {
            // Get school owner email
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

            // Send email to school owner
            Map<Placeholder, String> detail = Map.of(Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id, Placeholder.TITLE, "Admin Approve School");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(APPROVE_MAIL_ID).detail(detail).build());

            return ResponseEntity.accepted().body("Approve success");
        } else return ResponseEntity.badRequest().body("Approve failed");
    }
}