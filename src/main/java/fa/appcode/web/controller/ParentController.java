package fa.appcode.web.controller;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.EnrollSchoolInfoVo;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.exceptions.CustomDataException;
import fa.appcode.services.AccountService;
import fa.appcode.services.EnrollSchoolService;
import fa.appcode.services.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SchoolInfoService schoolInfoService;

    @Autowired
    private GlobalConfig globalConfig;

    @GetMapping({"parent-list", "parent-list/parent-details"})
    public String parentList(@RequestParam(name = "currentPage"
                                     , defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage,
                             @RequestParam(defaultValue = Constant.KEY_WORD_DEFAULT) String search, Model model,
                             Principal principal, RedirectAttributes redirectAttributes) {
        try {
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
            if (role.equals(Constant.ADMIN_ROLE)) {
                list = accountService.findAllParent(search, pageable);
            } else if (role.equals(Constant.SCHOOL_OWNER_ROLE)) {
                list = accountService.findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(principal.getName(), search, pageable);
            } else {
                list = Page.empty();
            }
            List<ParentVo> parents = list.getContent();
            Log4jUtils.getLogger().info("Inside parentList Content : " + list);
            Log4jUtils.getLogger().info("Number Of pages : " + list.getTotalPages());
            /*
             * Put data into Model
             */
            model.addAttribute("accounts", parents);
            model.addAttribute("search", search);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("numberPage", list.getTotalPages());
            model.addAttribute("role", role);
        } catch (CustomDataException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        /*
         * Return view name
         */
        return "admin_side/parent-list";
    }

    @GetMapping("parent-list/parent-details/{id}")
    public String parentDetailsAdmin(@RequestParam(name = "currentPage"
                                             , defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage, @PathVariable("id") int id, Model model, Principal principal,
                                     RedirectAttributes redirectAttributes) {

        /*
         * Setup pageable
         */
        try {
            Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(),
                    Sort.by("id").ascending());
            /*
             * Get page from Service
             */
            String role = accountService.findAccountRoleString(principal.getName());
            Page<EnrolledSchoolVo> listParentEnroll;
            List<EnrollSchoolInfoVo> schoolInfoList;

            if (Constant.ADMIN_ROLE.equals(role)) {
                //get Data for Admin Role
                listParentEnroll = enrollSchoolService.findParentEnrolledSchoolByParentId(id, pageable);
                schoolInfoList = schoolInfoService.findAllSchoolPublished();
            } else if (Constant.SCHOOL_OWNER_ROLE.equals(role)) {
                //get Data for School Owner Role
                listParentEnroll = enrollSchoolService.findParentEnrolledSchoolByParentIdAndSchoolOwner(id, principal.getName(), pageable);
                schoolInfoList = schoolInfoService.findSchoolInfoListByAccountEmail(principal.getName());
            } else {
                Log4jUtils.getLogger().warn("There No Role, the List is empty");
                listParentEnroll = Page.empty();
                schoolInfoList = Collections.emptyList();
            }

            //Get List of Enroll School and get Parent Data
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
        } catch (IllegalAccessException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:" + Constant.PARENT_LIST_URL;
        }
        return "admin_side/parent-details";
    }

    @PostMapping({"parent-list/parent-details/{id}"})
    public String enrollAndUnenrollParentToSchool(@PathVariable("id") int id, @RequestParam(value = "school", required = false) Integer schoolId,
                                                  RedirectAttributes redirectAttributes, Principal principal, @RequestParam("actionType") String actionType,
                                                  @RequestParam(value = "enroll", required = false) Integer enrollId, @RequestParam(value = "recordNo", required = false) Integer recordNo){
        /*
         * get String role URL
         */

        String role = accountService.findAccountRoleString(principal.getName());
        String normalizedRole = role.toUpperCase().trim().replace(" ", "_");

        /*
         * Enroll Parent to School if action = enroll
         */
        try {
            if (Constant.ENROLL_PARENT_SCHOOL.equals(actionType)) {
                //Enroll Parent to School
                Log4jUtils.getLogger().info("Enrolling Parent: ");
                enrollSchoolService.enrollSchoolParent(accountService.getAccountInfoById(id), schoolInfoService.getSchoolInfoById(schoolId), LocalDate.now(), normalizedRole, principal);
                //Add FlashAttribute into redirectAttribute
                redirectAttributes.addFlashAttribute("message", globalConfig.getEnrollSuccess());
                redirectAttributes.addFlashAttribute("alertType", Constant.SUCCESS);
                Log4jUtils.getLogger().info("Enroll Parent successful to School");
            } else if (Constant.UNENROLL_PARENT_SCHOOL.equals(actionType)) {
                EnrollSchool enrollSchool = enrollSchoolService.findEnrollSchoolById(enrollId);
                //unenroll Parent
                Log4jUtils.getLogger().info("Unenrolling Parent: ");
                enrollSchoolService.evaluateParentEnroll(enrollSchool, LocalDate.now(), normalizedRole, Constant.ENROLL_STATUS_UNENROLL, principal, recordNo);
                redirectAttributes.addFlashAttribute("message", "You have Unenroll parent from " + enrollSchool.getSchool().getSchoolName());
                redirectAttributes.addFlashAttribute("alertType", Constant.DANGER);
                Log4jUtils.getLogger().info("Unenroll Parent Success");
            } else {
                redirectAttributes.addFlashAttribute("alertType", Constant.DANGER);
                redirectAttributes.addFlashAttribute("message", "Invalid action Type");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", Constant.DANGER);
        }

        /*
         * Return view name
         */
        return "redirect:" + Constant.VIEW_PARENT_DETAIL_URL + id;
    }

}
