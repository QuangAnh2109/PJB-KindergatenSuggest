package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.EnrollSchoolService;
import fa.appcode.services.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/")
public class ParentController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private EnrollSchoolService enrollSchoolService;
    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private SchoolInfoService schoolInfoService;
    @Value("${ME_013}")
    private String me13;


    @GetMapping({"manager/parent-list", "manager/parent-list/parent-details"})
    public String parentList(@RequestParam(name = "currentPage"
              ,defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage,
                             @RequestParam(defaultValue = Constant.KEY_WORD_DEFAULT) String search, Model model,
                             @RequestHeader(value = "X-Requested-With", required = false) String requestedWith, Principal principal) {

        Log4jUtils.getLogger().info("Inside parentList Method");
        Log4jUtils.getLogger().info(principal.getName());
        /*
         * Setup pageable
         */
        Log4jUtils.getLogger().info("Inside parentList Content : ");
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(),
                  Sort.by("id").ascending());
        /*
         * Get page from Service
         */
        Page<ParentVo> list = accountService.findAllParent(search,pageable);
        List<ParentVo> accounts = list.getContent();
        Log4jUtils.getLogger().info("Inside parentList Content : "+list.toString());
        /*
         * Put data into Model
         */
        model.addAttribute("accounts", accounts);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", list.getTotalPages());
        /*
         * Return view name
         */
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin_side/parent-list :: main-content";
        }
        return "admin_side/parent-list";
    }


    @GetMapping("manager/parent-list/parent-details/{id}")
    public String parentDetailsAdmin(@RequestParam(name = "currentPage"
            ,defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage, @PathVariable("id") int id, Model model,Principal principal,
                                     @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

        /*
         * Setup pageable
         */
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(),
                Sort.by("id").ascending());
        /*
         * Get page from Service
         */
        String role = accountService.findAccountRoleString(principal.getName());
        Page<EnrolledSchoolVo> listParentEnroll = Page.empty();
        List<SchoolInfo> schoolInfoList = Collections.emptyList();


        if("Admin".equals(role)){

            listParentEnroll = enrollSchoolService.findParentEnrolledSchoolByParentId(id,pageable);
            schoolInfoList=  schoolInfoService.findAllSchoolPublished();

        } else if("School owner".equals(role)){

            listParentEnroll = enrollSchoolService.findParentEnrolledSchoolByParentIdAndSchoolOwner(id, principal.getName(), pageable);
            schoolInfoList = schoolInfoService.findSchoolInfoListByAccountEmail(principal.getName());

        }


        List<EnrolledSchoolVo> enrolledSchools = listParentEnroll.getContent();
        ParentVo accountInfo = accountService.findParentById(id);

        /*
         * Put data into Model
         */
        model.addAttribute("schoolInfoList", schoolInfoList);
        model.addAttribute("enrolledSchools", enrolledSchools);
        model.addAttribute("accountInfo", accountInfo);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", listParentEnroll.getTotalPages());
        model.addAttribute("role", role);
        /*
         * Return view name
         */
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin_side/parent-details :: main-content";
        }
        return "admin_side/parent-details";
    }


//    @GetMapping("school-owner/parent-list/parent-details/{id}")
//    public String parentDetailsSchoolOwner(@RequestParam(name = "currentPage"
//            ,defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage, @PathVariable("id") int id, Model model, Principal principal,
//                                           @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
//        /*
//         * Setup pageable
//         */
//        Pageable pageable = PageRequest.of(currentPage, 5,
//                Sort.by("id").ascending());
//        /*
//         * Get page from Service
//         */
//        ParentVo accountInfo = accountService.findParentById(id);
//        Page<EnrolledSchoolVo> listParentEnroll = accountService.findParentEnrolledSchoolByParentIdAndSchoolOwner(id, principal.getName(), pageable);
//        List<EnrolledSchoolVo> enrolledSchools = listParentEnroll.getContent();
//        /*
//         * Get Schools from Service
//         */
//        List<SchoolInfo> schoolInfoList = schoolInfoService.findSchoolInfoByAccountId(principal.getName());
//        String role = accountService.findAccountRoleString(principal.getName()).replaceAll(" ","-").toLowerCase();
//        /*
//         * Put data into Model
//         */
//        if (accountInfo == null) {
//            throw new IllegalArgumentException("Parent not found for ID: " + id);
//        }
//        model.addAttribute("role", role);
//        model.addAttribute("schoolInfoList", schoolInfoList);
//        model.addAttribute("enrolledSchools", enrolledSchools);
//        model.addAttribute("accountInfo", accountInfo);
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("numberPage", listParentEnroll.getTotalPages());
//        /*
//         * Return view name
//         */
//        if ("XMLHttpRequest".equals(requestedWith)) {
//            return "admin_side/parent-details:: main-content";
//        }
//        return "admin_side/parent-details";
//    }


    @PostMapping({"manager/parent-list/parent-details/{id}"})
    public String enrollParentToSchool(@PathVariable("id") int id, Model model, @RequestParam(value = "school", required = false) Integer schoolId,
                                       RedirectAttributes redirectAttributes, Principal principal, @RequestParam("actionType") String actionType,
                                       @RequestParam(value = "enroll", required = false) Integer enrollId) {
        /*
         * get String role URL
         */

        String role = accountService.findAccountRoleString(principal.getName());

        /*
         * Enroll Parent to School if action = enroll
         */

        if("enroll".equals(actionType)) {
            try {
                // Enroll Parent to School
                Log4jUtils.getLogger().info("Enroll parent to school: ");
                EnrollSchool schoolEnroll = new EnrollSchool();
                schoolEnroll.setAccount(accountService.getAccountInfoById(id));
                Log4jUtils.getLogger().info("Parent account ID: "+schoolEnroll.getAccount().getId());
                schoolEnroll.setSchool(schoolInfoService.getSchoolInfoById(schoolId));
                Log4jUtils.getLogger().info("Parent School ID Enrolling: "+schoolEnroll.getSchool().getId());
                schoolEnroll.setEnrollDate(LocalDate.now());
                Log4jUtils.getLogger().info("Parent Enroll Date: "+schoolEnroll.getEnrollDate());
                schoolEnroll.setStatus(true);
                Log4jUtils.getLogger().info("Parent Enroll Status: "+schoolEnroll.getStatus());
                //change with LocalDate time zone
//                schoolEnroll.setCreateTime(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
//                schoolEnroll.setUpdateTime(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
                schoolEnroll.setCreateTime(Instant.now());
                Log4jUtils.getLogger().info("Create Enroll Date: "+schoolEnroll.getCreateTime());
                schoolEnroll.setUpdateTime(Instant.now());
                Log4jUtils.getLogger().info("Update Enroll Date: "+schoolEnroll.getUpdateTime());
                schoolEnroll.setDeleteFlg(false);
                if(role.equals("School owner")) {
                    schoolEnroll.setCreateId("SCHOOL_OWNER");
                    schoolEnroll.setUpdateId("SCHOOL_OWNER");
                }
                if(role.equals("Admin")) {
                    schoolEnroll.setCreateId("ADMIN");
                    schoolEnroll.setUpdateId("ADMIN");

                }
                Log4jUtils.getLogger().info("Account That Create Enrollment: "+schoolEnroll.getCreateId());

                if(role.equals("School owner")) {
                    List<Integer> schoolIdList = schoolInfoService.getAllSchoolIdsByAccountEmail(principal.getName());
                    if(!schoolIdList.contains(schoolId)) {
                        Log4jUtils.getLogger().info("School Owner try to Enroll School That Not in Permission: "+schoolEnroll.getAccount().getId());

                        throw new Exception();
                    }
                }
                enrollSchoolService.enrollSchool(schoolEnroll);
                // Success message
                redirectAttributes.addFlashAttribute("message", me13);
                redirectAttributes.addFlashAttribute("alertType", "success");
                Log4jUtils.getLogger().info("message and type of alert if enrollment if enrollment successful " + me13 + " ,success");
            } catch (Exception e) {
                // Error message
                redirectAttributes.addFlashAttribute("message", e);
                redirectAttributes.addFlashAttribute("alertType", "danger");
                Log4jUtils.getLogger().info("Exception if enrollment fail or error because of method: " + e);

            }
        } else
            /*
             * UnEnroll Parent to School if action = unenroll
             */
            if ("unenroll".equals(actionType)) {
                try{
                    EnrollSchool enrollSchool = enrollSchoolService.findEnrollSchoolById(enrollId);
                    enrollSchool.setEnrollEndDate(LocalDate.now());
                    enrollSchool.setUpdateTime(Instant.now());
                    enrollSchool.setStatus(false);
                    enrollSchool.setRecordNo(enrollSchool.getRecordNo() + 1);
                    if(role.equals("School owner")) {
                        enrollSchool.setUpdateId("SCHOOL_OWNER");
                    }
                    if(role.equals("Admin")) {
                        enrollSchool.setUpdateId("ADMIN");
                    }
                    if(role.equals("School owner")) {
                        List<Integer> schoolIdList = schoolInfoService.getAllSchoolIdsForUnenrollParentByAccountEmail(principal.getName());
                        if(!schoolIdList.contains(enrollSchool.getSchool().getId())) {
                            throw new Exception();
                        }
                    }
                    enrollSchoolService.enrollSchool(enrollSchool);
                    redirectAttributes.addFlashAttribute("message", "You have been successfully unenrolled parent to " + enrollSchool.getSchool().getSchoolName());
                    redirectAttributes.addFlashAttribute("alertType", "success");
                }
                 catch (Exception e) {
                     redirectAttributes.addFlashAttribute("message", e);
                     redirectAttributes.addFlashAttribute("alertType", "danger");
                }
            }

        /*
         * Return view name
         */
        return "redirect:/manager/parent-list/parent-details/" + id;
    }

}
