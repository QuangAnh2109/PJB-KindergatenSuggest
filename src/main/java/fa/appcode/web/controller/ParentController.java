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
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.*;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/manager/")
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


    @GetMapping({"parent-list", "parent-list/parent-details"})
    public String parentList(@RequestParam(name = "currentPage"
                                     , defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage,
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
        String role = accountService.findAccountRoleString(principal.getName());

        Page<ParentVo> list;
        if (role.equals("Admin")) {
            list = accountService.findAllParent(search, pageable);
        } else if (role.equals("School owner")) {
            list = accountService.findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(principal.getName(), search, pageable);
        } else {
            list = Page.empty();
        }
        List<ParentVo> accounts = list.getContent();
        Log4jUtils.getLogger().info("Inside parentList Content : " + list);
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


    @GetMapping("parent-list/parent-details/{id}")
    public String parentDetailsAdmin(@RequestParam(name = "currentPage"
                                             , defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage, @PathVariable("id") int id, Model model, Principal principal,
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
        Page<EnrolledSchoolVo> listParentEnroll;
        List<SchoolInfo> schoolInfoList;
        List<EnrolledSchoolVo> requestList;

        if ("Admin".equals(role)) {

            listParentEnroll = enrollSchoolService.findParentEnrolledSchoolByParentId(id, pageable);
            schoolInfoList = schoolInfoService.findAllSchoolPublished();
            requestList = enrollSchoolService.findParentRequestEnrolledSchoolByParentId(id);
        } else if ("School owner".equals(role)) {

            listParentEnroll = enrollSchoolService.findParentEnrolledSchoolByParentIdAndSchoolOwner(id, principal.getName(), pageable);
            schoolInfoList = schoolInfoService.findSchoolInfoListByAccountEmail(principal.getName());
            requestList=enrollSchoolService.findParentRequestEnrollSchoolByParentIdAndSchoolOwner(id, principal.getName());
        } else {
            listParentEnroll = Page.empty();
            schoolInfoList = Collections.emptyList();
            requestList = Collections.emptyList();
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
        model.addAttribute("requestList", requestList);
        model.addAttribute("numberPage", listParentEnroll.getTotalPages());
        /*
         * Return view name
         */
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin_side/parent-details :: main-content";
        }
        return "admin_side/parent-details";
    }

    @PostMapping({"parent-list/parent-details/{id}"})
    public String enrollParentToSchool(@PathVariable("id") int id, @RequestParam(value = "school", required = false) Integer schoolId,
                                       RedirectAttributes redirectAttributes, Principal principal, @RequestParam("actionType") String actionType,
                                       @RequestParam(value = "enroll", required = false) Integer enrollId) throws Exception {
        /*
         * get String role URL
         */

        String role = accountService.findAccountRoleString(principal.getName());
        String normalizedRole = role.toUpperCase().trim().replace(" ", "_");

        /*
         * Enroll Parent to School if action = enroll
         */
//        if ("enroll".equals(actionType)) {
//            try {
//                // Enroll Parent to School
//                if (role.equals("School owner")) {
//                    List<Integer> schoolIdList = schoolInfoService.getAllSchoolIdsByAccountEmail(principal.getName());
//                    if (!schoolIdList.contains(schoolId)) {
//                        throw new Exception();
//                    }
//                    System.out.println("schoolIdList : " + schoolIdList);
//                }
//                enrollSchoolService.enrollSchoolParent(accountService.getAccountInfoById(id), schoolInfoService.getSchoolInfoById(schoolId), LocalDate.now(), normalizedRole);
//                // Success message
//                redirectAttributes.addFlashAttribute("message", me13);
//                redirectAttributes.addFlashAttribute("alertType", "success");
//                Log4jUtils.getLogger().info("message and type of alert if enrollment if enrollment successful " + me13 + " ,success");
//            } catch (Exception e) {
//                // Error message
//                redirectAttributes.addFlashAttribute("message", e);
//                redirectAttributes.addFlashAttribute("alertType", "danger");
//                Log4jUtils.getLogger().info("Exception if enrollment fail or error becauase of method: " + e);
//
//            }
//        } else
//            /*
//             * UnEnroll Parent to School if action = unenroll
//             */
//            if ("unenroll".equals(actionType)) {
//                try {
//                    EnrollSchool enrollSchool = enrollSchoolService.findEnrollSchoolById(enrollId);
//                    enrollSchoolService.execute(actionType,enrollSchool, LocalDate.now(), normalizedRole, principal);
//                    redirectAttributes.addFlashAttribute("message", "You have been successfully unenrolled parent to " + enrollSchool.getSchool().getSchoolName());
//                    redirectAttributes.addFlashAttribute("alertType", "success");
//                } catch (Exception e) {
//                    redirectAttributes.addFlashAttribute("message", e);
//                    redirectAttributes.addFlashAttribute("alertType", "danger");
//                }
//            }
            //GET ENROLL SCHOOL
            EnrollSchool enrollSchool = enrollSchoolService.findEnrollSchoolById(enrollId);
            //EXECUTE ACTION BASE ON ACTION TYPE UNENROLL, APPROVE, REJECT
            String result=enrollSchoolService.execute(actionType,enrollSchool, LocalDate.now(), normalizedRole, principal);
            if(result!=null){
                redirectAttributes.addFlashAttribute("message", "You have been successfully "+result+" parent to " + enrollSchool.getSchool().getSchoolName());
                redirectAttributes.addFlashAttribute("alertType", "success");
            } else {
                redirectAttributes.addFlashAttribute("alertType", "danger");
            }

        /*
         * Return view name
         */
        return "redirect:/manager/parent-list/parent-details/" + id;
    }

}
