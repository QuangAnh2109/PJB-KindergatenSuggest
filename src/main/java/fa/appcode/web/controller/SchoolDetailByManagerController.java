package fa.appcode.web.controller;

import fa.appcode.common.utils.MailConstant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.AccountService;
import fa.appcode.services.SchoolDetailManagerService;
import fa.appcode.services.SchoolInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/view-detail")
    public String getSchoolDetail(@RequestParam("id") int id, Model model) {
        return schoolDetailManagerService.getSchoolDetail(model, id);
    }

    @ResponseBody
    @PostMapping("/delete")
    public ResponseEntity<String> deleteSchool(@RequestParam("schoolId") int id, @RequestParam("recordNo") int recordNo) {
        List<Integer> inStatus = List.of(SchoolConstant.STATUS_SAVED, SchoolConstant.STATUS_SUBMITTED, SchoolConstant.STATUS_APPROVED, SchoolConstant.STATUS_REJECTED, SchoolConstant.STATUS_PUBLISHED, SchoolConstant.STATUS_UNPUBLISHED);
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_DELETED, inStatus, null, null, null, null);
    }

    @ResponseBody
    @PostMapping("/update")
    public ResponseEntity updateSchool(@Valid @RequestBody SchoolFormManager schoolFormManager) {

        return ResponseEntity.badRequest().body("Update failed");
    }

    @ResponseBody
    @PostMapping("/public")
    public ResponseEntity publicSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
        // Get school owner email
        String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

        List<Integer> inStatus = List.of(SchoolConstant.STATUS_APPROVED, SchoolConstant.STATUS_UNPUBLISHED);
        Map<Placeholder, String> detail = Map.of(Placeholder.TITLE, "Admin Public School", Placeholder.SCHOOL_NAME, schoolInfoService.getSchoolNameBySchoolIdAndNoDelete(id), Placeholder.USER_NAME, accountService.getAccountNameByEmailAndNoDelete(SecurityContextHolder.getContext().getAuthentication().getName()), Placeholder.LINK, globalConfig.getServerLink() + "/school-owner/school/detail/" + id);
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_PUBLISHED, inStatus, MailConstant.MAIL_PUBLISH_SCHOOL, detail, List.of(email), List.of());
    }

    @ResponseBody
    @PostMapping("/unpublic")
    public ResponseEntity unpublicSchool(@RequestParam("id") int id, @RequestParam("recordNo") int recordNo) {
        return schoolDetailManagerService.changeSchoolStatus(id, recordNo, SchoolConstant.STATUS_UNPUBLISHED, List.of(SchoolConstant.STATUS_PUBLISHED), null, null, null, null);
    }
}
