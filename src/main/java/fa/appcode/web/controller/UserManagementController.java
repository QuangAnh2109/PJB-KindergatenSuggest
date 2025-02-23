package fa.appcode.web.controller;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/")
public class UserManagementController {
    @Autowired
    AccountService accountService;

    @GetMapping("userlist")
    public String getUserList(@RequestParam(defaultValue ="") String search,
                              @RequestParam(defaultValue = "0") int currentPage,
                              Model model) {

        Pageable pageable = PageRequest.of(currentPage, 10);

        Page<AccountVo> accounts = accountService.getAllAccounts(search, pageable);
        List<AccountVo> listAccount = accounts.getContent();
        model.addAttribute("listAccounts", listAccount);
        model.addAttribute("searchField", search);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", accounts.getTotalPages());

        return "admin_side/UserList";
    }


    @GetMapping("adduser")
    public String admin_addUser() {
        return "admin_side/AddUser";
    }

    @GetMapping("edituser")
    public String admin_editUser() {
        return "admin_side/EditUser";
    }

    @GetMapping("userdetail")
    public String admin_userDetail() {
        return "admin_side/UserDetails";
    }
}
