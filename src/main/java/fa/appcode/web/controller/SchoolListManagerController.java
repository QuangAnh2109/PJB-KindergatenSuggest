package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.vo.SchoolListManager;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolInfoService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class SchoolListManagerController {

    private final SchoolInfoService schoolInfoService;

    private final MasterDatumService masterDatumService;

    @GetMapping("/manager/school-list")
    public String getSchoolListManager() {
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals(Constant.ADMIN_ROLE)) {
                return "redirect:/admin/school-list";
            } else {
                break;
            }
        }
        return "redirect:/school-owner/school-list";
    }

    @GetMapping("/admin/school-list")
    public String getSchoolListManagerByAdmin(Model model) {
        model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndPagingAndDeleteFlg(Constant.PAGE_DEFAULT, Constant.SEARCH_ALL));
        model.addAttribute("isAdmin", true);
        model.addAttribute("status", masterDatumService.findAllByTypeNameNoDelete(SchoolConstant.SCHOOL_STATUS));
        return Constant.SCHOOL_LIST_MANAGER_PAGE;
    }

    @GetMapping("/admin/school-list/searchAndPaging")
    public String getSchoolListManagerByAdminSearch(@RequestParam("search") String search, @RequestParam("page") int page, Model model) {
        model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndPagingAndDeleteFlg(page, search));
        model.addAttribute("isAdmin", true);
        model.addAttribute("status", masterDatumService.findAllByTypeNameNoDelete(SchoolConstant.SCHOOL_STATUS));
        return Constant.SCHOOL_LIST_MANAGER_PAGE + " :: main-content";
    }

    @GetMapping("/admin/school-list/second")
    public String getSchoolListManagerByAdminSearchReload(@RequestParam("search") String search, @RequestParam("page") int page, Model model) {
        model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndPagingAndDeleteFlg(page, search));
        model.addAttribute("isAdmin", true);
        model.addAttribute("status", masterDatumService.findAllByTypeNameNoDelete(SchoolConstant.SCHOOL_STATUS));
        return Constant.SCHOOL_LIST_MANAGER_PAGE + " :: main-content";
    }

    @GetMapping("/school-owner/school-list")
    public String getSchoolListManagerBySchoolOwner(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndAccountAndPagingAndDeleteFlg(Constant.PAGE_DEFAULT, Constant.SEARCH_ALL, email));
        model.addAttribute("isAdmin", false);
        model.addAttribute("status", masterDatumService.findAllByTypeNameNoDelete(SchoolConstant.SCHOOL_STATUS));
        return Constant.SCHOOL_LIST_MANAGER_PAGE;
    }

    @GetMapping("/school-owner/school-list/searchAndPaging")
    public String getSchoolListManagerBySchoolOwnerSearch(@RequestParam("search") String search, @RequestParam("page") int page, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndAccountAndPagingAndDeleteFlg(page, search, email));
        model.addAttribute("isAdmin", true);
        model.addAttribute("status", masterDatumService.findAllByTypeNameNoDelete(SchoolConstant.SCHOOL_STATUS));
        return Constant.SCHOOL_LIST_MANAGER_PAGE + " :: main-content";
    }

    @GetMapping("/school-owner/school-list/second")
    public String getSchoolListManagerBySchoolOwnerSearchReload(@RequestParam("search") String search, @RequestParam("page") int page, Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndAccountAndPagingAndDeleteFlg(page, search, email));
        model.addAttribute("isAdmin", true);
        model.addAttribute("status", masterDatumService.findAllByTypeNameNoDelete(SchoolConstant.SCHOOL_STATUS));
        return Constant.SCHOOL_LIST_MANAGER_PAGE + " :: main-content";
    }
}