package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.AccountService;
import fa.appcode.services.EnrollSchoolService;
import fa.appcode.services.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
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


    @GetMapping({"admin/parent-list", "school-owner/parent-list","admin/parent-list/parent-details", "school-owner/parent-list/parent-details"})
    public String parentList(@RequestParam(name = "currentPage"
              ,defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage, @RequestParam(defaultValue = Constant.KEY_WORD_DEFAULT) String search, Model model, @RequestHeader(value = "X-Requested-With", required = false) String requestedWith, Principal principal) {

        /*
         * Setup pageable
         */
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(),
                  Sort.by("id").ascending());
        /*
         * Get page from Service
         */
        Page<ParentVo> list = accountService.findAllParent(search,pageable);
        List<ParentVo> accounts = list.getContent();
        String role = accountService.findByEmail(principal.getName()).getAccountRole().replaceAll(" ","-").toLowerCase();
        /*
         * Put data into Model
         */
        model.addAttribute("role", role);
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


    @GetMapping("admin/parent-list/parent-details/{id}")
    public String parentDetailsAdmin(@RequestParam(name = "currentPage"
            ,defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage, @PathVariable("id") int id, Model model,Principal principal,  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

        /*
         * Setup pageable
         */
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(),
                Sort.by("id").ascending());
        /*
         * Get page from Service
         */
        ParentVo accountInfo = accountService.findParentById(id);
        Page<EnrolledSchoolVo> listParentEnroll = accountService.findParentEnrolledSchoolByParentId(id,pageable);
        List<EnrolledSchoolVo> enrolledSchools = listParentEnroll.getContent();
        String role = accountService.findByEmail(principal.getName()).getAccountRole().replaceAll(" ","-").toLowerCase();

        /*
         * Get Schools from Service
         */
        List<SchoolInfo> schoolInfoList = schoolInfoService.findAll();
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


    @GetMapping("school-owner/parent-list/parent-details/{id}")
    public String parentDetailsSchoolOwner(@RequestParam(name = "currentPage"
            ,defaultValue = Constant.SCHOOL_AND_ENROLL_INIT_PAGE) int currentPage, @PathVariable("id") int id, Model model, Principal principal,  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        /*
         * Setup pageable
         */
        Pageable pageable = PageRequest.of(currentPage, 5,
                Sort.by("id").ascending());
        /*
         * Get page from Service
         */
        ParentVo accountInfo = accountService.findParentById(id);
        Page<EnrolledSchoolVo> listParentEnroll = accountService.findParentEnrolledSchoolByParentIdAndSchoolOwner(id, principal.getName(), pageable);
        List<EnrolledSchoolVo> enrolledSchools = listParentEnroll.getContent();
        /*
         * Get Schools from Service
         */
        List<SchoolInfo> schoolInfoList = schoolInfoService.findSchoolInfoByAccountId(principal.getName());
        String role = accountService.findByEmail(principal.getName()).getAccountRole().replaceAll(" ","-").toLowerCase();
        /*
         * Put data into Model
         */
        model.addAttribute("role", role);
        model.addAttribute("schoolInfoList", schoolInfoList);
        model.addAttribute("enrolledSchools", enrolledSchools);
        model.addAttribute("accountInfo", accountInfo);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", listParentEnroll.getTotalPages());
        /*
         * Return view name
         */
        System.out.println(Instant.now());
        if ("XMLHttpRequest".equals(requestedWith)) {
            return "admin_side/parent-details:: main-content";
        }
        return "admin_side/parent-details";
    }


    @Transactional
    @PostMapping({"school-owner/parent-list/parent-details/{id}", "admin/parent-list/parent-details/{id}"})
    public String enrollParentToSchool(@PathVariable("id") int id, Model model, @RequestParam("school") int schoolId, RedirectAttributes redirectAttributes, Principal principal) {
        /*
         * Enroll Parent to School
         */
        String role = accountService.findByEmail(principal.getName()).getAccountRole().replaceAll(" ","-").toLowerCase();
        try {
            // Enroll Parent to School
            EnrollSchool schoolEnroll = new EnrollSchool();
            schoolEnroll.setAccount(accountService.getAccountInfoById(id));
            schoolEnroll.setSchool(schoolInfoService.getSchoolInfoById(schoolId));
            schoolEnroll.setEnrollDate(LocalDate.now());
            schoolEnroll.setStatus(true);
            schoolEnroll.setCreateTime(Instant.now());
            schoolEnroll.setUpdateTime(Instant.now());
            schoolEnroll.setDeleteFlg(false);
            if(role.equals("school-owner")) {
                schoolEnroll.setCreateId("SCHOOL_OWNER");
                schoolEnroll.setUpdateId("SCHOOL_OWNER");
            }
            if(role.equals("admin")) {
                schoolEnroll.setCreateId("ADMIN");
                schoolEnroll.setUpdateId("ADMIN");
            }
            enrollSchoolService.enrollSchool(schoolEnroll);

            // Success message
            redirectAttributes.addFlashAttribute("message", "Parent enrolled successfully to " + schoolEnroll.getSchool().getSchoolName());
            redirectAttributes.addFlashAttribute("alertType", "success");
        } catch (Exception e) {
            // Error message
            redirectAttributes.addFlashAttribute("message", "Failed to enroll parent!");
            redirectAttributes.addFlashAttribute("alertType", "danger");
        }
        /*
         * Return view name
         */
        return "redirect:/"+role+"/parent-list/parent-details/" + id;
    }

}
