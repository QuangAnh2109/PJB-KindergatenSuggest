package fa.appcode.web.controller;

import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import fa.appcode.services.SchoolInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class SchoolAdminController {

    private final SchoolInfoService schoolInfoService;

    private final EmailService emailService;

    private final AccountService accountService;

    private final GlobalConfig globalConfig;

    @ResponseBody
    @PostMapping("/school/delete/{id}")
    public String deleteSchoolByAdmin(@PathVariable("id") int id) {
        return "";
    }

    @GetMapping("/school/detail/{id}")
    public String getSchoolDetailByAdmin(@PathVariable("id") int id) {
        return "";
    }

    @ResponseBody
    @PostMapping("/school/update/{id}")
    public String updateSchoolByAdmin(@PathVariable("id") int id) {
        return "";
    }

    @ResponseBody
    @PatchMapping("/school/submit/{id}")
    public ResponseEntity addSchoolBySchoolOwner(@PathVariable("id") int id) {
        try{
            // Find school info by id
            SchoolInfo schoolInfo = schoolInfoService.findSchoolInfoByIdNoDelete(id);

            // Check if school info is null
            if(schoolInfo != null) {
                return ResponseEntity.badRequest().body("School not found");
            }

            // Check if school info is not in saved status
            if(schoolInfo.getStatusId() != 1) {
                return ResponseEntity.badRequest().body("School not in saved status");
            }

            // Set status to approve
            schoolInfo.setStatusId(3);

            // Increase record no
            schoolInfo.setRecordNo(schoolInfo.getRecordNo() + 1);
            schoolInfo.setUpdateId("USER_ADMIN");
            schoolInfo.setUpdateTime(Instant.now());

            // Save school info to database
            schoolInfoService.save(schoolInfo);

            // Send email to school owner
            String email = accountService.getEmailByAccountIdAndActiveAndNoDelete(schoolInfo.getAccount().getId());
            Map<Placeholder, String> detail = Map.of(Placeholder.TITLE, "School approved", Placeholder.LINK, (globalConfig.getServerLink()+"/school/detail/"+schoolInfo.getId()));
            SendMailInfo sendMailInfo = SendMailInfo.builder().toMail(List.of(email)).ccMail(List.of()).mailId(4).detail(detail).build();
            emailService.sendEmailToMany(sendMailInfo);

            return ResponseEntity.accepted().body("Submit success");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Submit failed " + e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/school/reject/{id}")
    public String rejectSchoolByAdmin(@PathVariable("id") int id) {
        return "";
    }

    @ResponseBody
    @PostMapping("/school/approve/{id}")
    public String approveSchoolByAdmin(@PathVariable("id") int id) {
        return "";
    }
}
