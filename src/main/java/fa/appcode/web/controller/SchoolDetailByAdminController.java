package fa.appcode.web.controller;

import fa.appcode.common.utils.*;
import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.config.GlobalConfig;
import fa.appcode.repositories.MasterDatumRepository;
import fa.appcode.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fa.appcode.common.utils.Constant.EMAIL_REGEX_HTML;
import static fa.appcode.common.utils.Constant.PHONE_REGEX_HTML;

@Controller
@RequestMapping("/admin/school")
@RequiredArgsConstructor
public class SchoolDetailByAdminController {

    private final SchoolInfoService schoolInfoService;

    private final MasterDatumService masterDatumService;

    private final MasterDatumRepository masterDatumRepository;

    private final EmailService emailService;

    private final AccountService accountService;

    private final GlobalConfig globalConfig;

    private final SchoolDetailAdminService schoolDetailAdminService;

    private final SchoolDetailManagerService schoolDetailManagerService;

    private final CityService cityService;

    @GetMapping("/view-detail")
    public String getSchoolDetail(@RequestParam("id") int id, Model model) {
        return schoolDetailAdminService.getSchoolDetail(model, id);
    }

    @ResponseBody
    @PostMapping("/delete")
    public ResponseEntity deleteSchool(@RequestParam("schoolId") int id, @RequestParam("recordNo") int recordNo) {
        if (schoolDetailAdminService.deleteSchoolByStatus(id, recordNo)) {
            return ResponseEntity.accepted().body(SchoolConstant.MESSAGE_DELETE_SUCCESS);
        } else return ResponseEntity.badRequest().body(SchoolConstant.MESSAGE_DELETE_FAIL);
    }

    @ResponseBody
    @PostMapping("/update")
    public ResponseEntity updateSchool(@Valid @RequestBody SchoolFormManager schoolFormManager) {
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
    @PostMapping("/submit")
    public ResponseEntity submitSchool(@RequestParam("schoolId") int id, @RequestParam("recordNo") int recordNo) {
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
    @PostMapping("/reject")
    public ResponseEntity rejectSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
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
    @PostMapping("/approve")
    public ResponseEntity approveSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
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
