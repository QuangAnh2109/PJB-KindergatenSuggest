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
        if (role.equals(Constant.ADMIN_ROLE)) {
            list = accountService.findAllParent(search, pageable);
        } else if (role.equals(Constant.SCHOOL_OWNER_ROLE)) {
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
        model.addAttribute("role", role);
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
                                     @RequestHeader(value = "X-Requested-With", required = false) String requestedWith, RedirectAttributes redirectAttributes) {

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

        if (Constant.ADMIN_ROLE.equals(role)) {

            listParentEnroll = enrollSchoolService.findParentEnrolledSchoolByParentId(id, pageable);
            schoolInfoList = schoolInfoService.findAllSchoolPublished();
            requestList = enrollSchoolService.findParentRequestEnrolledSchoolByParentId(id);
        } else if (Constant.SCHOOL_OWNER_ROLE.equals(role)) {

            listParentEnroll = enrollSchoolService.findParentEnrolledSchoolByParentIdAndSchoolOwner(id, principal.getName(), pageable);
            schoolInfoList = schoolInfoService.findSchoolInfoListByAccountEmail(principal.getName());
            requestList = enrollSchoolService.findParentRequestEnrollSchoolByParentIdAndSchoolOwner(id, principal.getName());
        } else {
            Log4jUtils.getLogger().warn("There No Role, the List is emoty");
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
        model.addAttribute("role", role);
        if (accountInfo == null) {
            redirectAttributes.addFlashAttribute("message","There No Parent Found!");
//            model.addAttribute("message", "There No Parent Found!");
            return "redirect:" + Constant.PARENT_LIST_URL;
        } else {
            /*
             * Return view name
             */
            if ("XMLHttpRequest".equals(requestedWith)) {
                return "admin_side/parent-details :: main-content";
            }
            return "admin_side/parent-details";
        }
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
        try {

            if (Constant.ENROLL_PARENT_SCHOOL.equals(actionType)) {
                if (role.equals(Constant.SCHOOL_OWNER_ROLE)) {
                    List<Integer> schoolIdList = schoolInfoService.getAllSchoolIdsByAccountEmail(principal.getName());
                    if (!schoolIdList.contains(schoolId)) {
                        throw new IllegalAccessException("Unauthorized action for this school.");
                    }
                }
                if(enrollSchoolService.isParentEnrollingToSchool(id, schoolId)) {
                    throw new IllegalAccessException("Parent is Already Enrolled To This school.");
                }
                enrollSchoolService.enrollSchoolParent(accountService.getAccountInfoById(id), schoolInfoService.getSchoolInfoById(schoolId), LocalDate.now(), normalizedRole);
            } else {
                EnrollSchool enrollSchool = enrollSchoolService.findEnrollSchoolById(enrollId);
                //EXECUTE ACTION BASE ON ACTION TYPE UNENROLL, APPROVE, REJECT
                String result = enrollSchoolService.execute(actionType, enrollSchool, LocalDate.now(), normalizedRole, principal);
                if (result.equals(Constant.APPROVE_ENROLL_REQUEST)) {
                    redirectAttributes.addFlashAttribute("message", "Enrolled the parent successfully into the school.");
                    redirectAttributes.addFlashAttribute("alertType", "success");
                } else if ((result.equals(Constant.REJECT_ENROLL_REQUEST) || result.equals(Constant.UNENROLL_PARENT_SCHOOL))) {
                    redirectAttributes.addFlashAttribute("message", "You have " + result + " parent to " + enrollSchool.getSchool().getSchoolName());
                    redirectAttributes.addFlashAttribute("action", result.toUpperCase());
                    redirectAttributes.addFlashAttribute("alertType", "danger");
                } else {
                    redirectAttributes.addFlashAttribute("alertType", "danger");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "danger");
        }

        /*
         * Return view name
         */
        return "redirect:" + Constant.VIEW_PARENT_DETAIL_URL + id;
    }

}
