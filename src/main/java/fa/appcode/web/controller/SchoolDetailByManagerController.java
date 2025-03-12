package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/school")
public class SchoolDetailByManagerController {
    @GetMapping("/view-detail")
    public String getSchoolDetail(@RequestParam("id") int id) {
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String role = authority.getAuthority();
            if (role.equals(Constant.ADMIN_ROLE)) {
                return "redirect:/admin/school/view-detail?id="+id;
            } else {
                break;
            }
        }
        return "redirect:/school-owner/school/view-detail?id="+id;
    }
}
