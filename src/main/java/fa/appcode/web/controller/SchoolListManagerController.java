package fa.appcode.web.controller;

import fa.appcode.common.vo.SchoolListManager;
import fa.appcode.services.AccountService;
import fa.appcode.services.SchoolInfoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class SchoolListManagerController {

    private final AccountService accountService;

    private final SchoolInfoService schoolInfoService;

    private final int PAGE_DEFAULT = 0;

    private final String SEARCH_ALL = "";

    @GetMapping("/schol-list")
    public String getSchoolListManager() {
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0] == "Admin"){
            return "redirect:/admin/school-list";
        }else return "redirect:/school-owner/school-list";
    }

    @GetMapping("/admin/school-list")
    public String getSchoolListManagerByAdmin(Model model) {
        model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndPagingAndDeleteFlg(PAGE_DEFAULT, SEARCH_ALL));
        model.addAttribute("isAdmin", true);
        return "school-list-manager";
    }

    @GetMapping("/school-owner/school-list")
    public String getSchoolListManagerBySchoolOwner(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndAccountAndPagingAndDeleteFlg(PAGE_DEFAULT, SEARCH_ALL, email));
        model.addAttribute("isAdmin", false);
        return "school-list-manager";
    }

    @ResponseBody
    @GetMapping("/admin/school-list/{page}")
    public List<SchoolListManager> getSchoolListPageManagerByAdmin(@PathVariable("page") int pageNumber, @RequestParam(name="search") String search) {
        return schoolInfoService.searchAllByNameAndPagingAndDeleteFlg(pageNumber, search);
    }

    @ResponseBody
    @GetMapping("/school-owner/school-list/{page}")
    public List<SchoolListManager> getSchoolListPageManagerBySchoolOwner(@PathVariable("page") int pageNumber, @RequestParam(name="search") String search) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return schoolInfoService.searchAllByNameAndAccountAndPagingAndDeleteFlg(pageNumber, search, email);
    }
}