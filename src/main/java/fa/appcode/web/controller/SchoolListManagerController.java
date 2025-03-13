package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.vo.SchoolListManager;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolInfoService;
import fa.appcode.services.SchoolListManagerService;
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

    private final SchoolListManagerService schoolListManagerService;

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
    public String getSchoolListManagerByAdmin(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, Model model) {
        return schoolListManagerService.setSchoolDataToModelBySearchAndPage(search, page, true, false, model);
    }

    @GetMapping("/admin/school-list/searchAndPaging")
    public String getSchoolListManagerByAdminSearch(@RequestParam("search") String search, @RequestParam("page") int page, Model model) {
        return schoolListManagerService.setSchoolDataToModelBySearchAndPage(search, page, true, true, model);
    }

    @GetMapping("/school-owner/school-list")
    public String getSchoolListManagerBySchoolOwner(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, Model model) {
        return schoolListManagerService.setSchoolDataToModelBySearchAndPage(search, page, false, false, model);
    }

    @GetMapping("/school-owner/school-list/searchAndPaging")
    public String getSchoolListManagerBySchoolOwnerSearch(@RequestParam("search") String search, @RequestParam("page") int page, Model model) {
        return schoolListManagerService.setSchoolDataToModelBySearchAndPage(search, page, false, true, model);
    }
}