package fa.appcode.web.controller;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


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

    @GetMapping("userdetail/{id}")
    public String userDetail(@PathVariable("id") Integer id, Model model) {
        AccountVo user = accountService.getAccountById(id);
        model.addAttribute("user", user);
        return "admin_side/UserDetails";
    }

    @PostMapping("userdetail/{id}/toggleStatus")
    public String toggleUserStatus(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            accountService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("message", "User status updated successfully.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
        }
        return "redirect:/admin/userdetail/" + id;
    }

}
