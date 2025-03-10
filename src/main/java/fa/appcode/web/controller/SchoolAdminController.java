package fa.appcode.web.controller;

import fa.appcode.common.utils.*;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class SchoolAdminController {

    private final SchoolInfoService schoolInfoService;

    private final MasterDatumService masterDatumService;

    private final EmailService emailService;

    private final AccountService accountService;

    private final GlobalConfig globalConfig;

    private final CityService cityService;

    private final SchoolDetailAdminService schoolDetailAdminService;


    @ResponseBody
    @PostMapping("/school/delete/{id}&{recordNo}")
    public ResponseEntity deleteSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        if (schoolDetailAdminService.deleteSchoolByStatus(id, recordNo)) {
            return ResponseEntity.accepted().body(SchoolConstant.MESSAGE_DELETE_SUCCESS);
        } else return ResponseEntity.badRequest().body(SchoolConstant.MESSAGE_DELETE_FAIL);
    }

    @GetMapping("/school/detail/{id}&{edit}")
    public String getSchoolDetailByAdmin(@PathVariable("id") int id, @PathVariable("edit") boolean edit, Model model) {
        SchoolFormManager school = schoolInfoService.getSchoolFormByIdAndNoDelete(id);
        model.addAttribute("school", school);
        return "admin_side/school-form";
    }

    @ResponseBody
    @PostMapping("/school/update")
    public ResponseEntity updateSchoolByAdmin(@Valid @RequestBody SchoolFormManager schoolFormManager) {
        // Get school info by id
        SchoolFormManager schoolInfo = schoolInfoService.getSchoolFormByIdAndNoDelete(schoolFormManager.getId());

        // Check schoolFormManager actually changed and same record
        if (!schoolInfo.equals(schoolFormManager) && schoolInfo.getRecordNo() == schoolFormManager.getRecordNo()) {


            if(!List.of(SchoolConstant.STATUS_DELETED, SchoolConstant.STATUS_REJECTED).contains(schoolInfo.getStatusId())) {

                // Set school status to approved
                if(!List.of(SchoolConstant.STATUS_SAVED, SchoolConstant.STATUS_SUBMITTED).contains(schoolInfo.getStatusId())) {
                    schoolFormManager.setStatusId(SchoolConstant.STATUS_APPROVED);
                }

                // Set user is admin
                schoolFormManager.setSchoolOwnerEmail(null);

                // Update school info
                if (schoolInfoService.updateSchoolInfoBySchoolFormManager(schoolFormManager) > 0) {
                    return ResponseEntity.accepted().body("Update success");
                }
            }
        }
        return ResponseEntity.badRequest().body("Update failed");
    }

    @ResponseBody
    @PostMapping("/school/submit/{id}&{recordNo}")
    public ResponseEntity submitSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        // Check school status is saved or submitted
        List<Integer> inStatus = List.of(SchoolConstant.STATUS_SAVED, SchoolConstant.STATUS_SUBMITTED);

        // Update school status
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, SchoolConstant.STATUS_APPROVED, RoleConstant.ADMIN, inStatus) > 0) {

            // Get school owner email
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

            // Send email to school owner
            Map<Placeholder, String> detail = Map.of(Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id + "&0", Placeholder.TITLE, "Admin Submit and Approve School");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(MailConstant.MAIL_APPROVE_SCHOOL).detail(detail).build());

            return ResponseEntity.accepted().body("Submit success");
        } else return ResponseEntity.badRequest().body("Submit failed");
    }

    @ResponseBody
    @PostMapping("/school/reject/{id}&{recordNo}")
    public ResponseEntity rejectSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        // Check school status is submitted
        List<Integer> inStatus = List.of(SchoolConstant.STATUS_SUBMITTED);

        // Update school status to rejected
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, SchoolConstant.STATUS_REJECTED, RoleConstant.ADMIN, inStatus) > 0) {
            // Get school owner email
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

            // Send email to school owner
            Map<Placeholder, String> detail = Map.of(Placeholder.TITLE, "Admin Reject School");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(MailConstant.MAIL_REJECT_SCHOOL).detail(detail).build());

            return ResponseEntity.accepted().body("Reject success");
        } else return ResponseEntity.badRequest().body("Reject failed");
    }


    @ResponseBody
    @PostMapping("/school/approve/{id}&{recordNo}")
    public ResponseEntity approveSchoolByAdmin(@PathVariable("id") int id, @PathVariable("recordNo") int recordNo) {
        // Check school status is submitted
        List<Integer> inStatus = List.of(SchoolConstant.STATUS_SUBMITTED);

        // Update school status to approved
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, SchoolConstant.STATUS_APPROVED, RoleConstant.ADMIN, inStatus) > 0) {
            // Get school owner email
            String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

            // Send email to school owner
            Map<Placeholder, String> detail = Map.of(Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id, Placeholder.TITLE, "Admin Approve School");
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(MailConstant.MAIL_APPROVE_SCHOOL).detail(detail).build());

            return ResponseEntity.accepted().body("Approve success");
        } else return ResponseEntity.badRequest().body("Approve failed");
    }


}