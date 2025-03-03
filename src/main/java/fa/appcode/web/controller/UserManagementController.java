package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.MasterDatum;
import fa.appcode.services.AccountService;
import fa.appcode.services.impl.MasterDatumServiceImpl;
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
import java.util.UUID;


@Controller
@RequestMapping("/admin/")
public class UserManagementController {
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    AccountService accountService;
    @Autowired
    private MasterDatumServiceImpl masterDatumService;

    // Display list of user account
    @GetMapping("user-list")
    public String getUserList(@RequestParam(defaultValue = Constant.KEY_WORD_DEFAULT) String search,
                              @RequestParam(defaultValue = Constant.USER_INIT_PAGE) int currentPage,
                              Model model) {

        Pageable pageable = PageRequest.of(currentPage,globalConfig.getSizeOfPage());

        Page<AccountVo> accounts = accountService.getAllAccounts(search, pageable);
        List<AccountVo> listAccount = accounts.getContent();
        model.addAttribute("listAccounts", listAccount);
        model.addAttribute("searchField", search);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", accounts.getTotalPages());

        return "admin_side/UserList";
    }


    @GetMapping("add-user")
    public String showAddUserPage(Model model) {
        model.addAttribute("user", new AccountVo()); // Gửi một user rỗng để form hiển thị đúng
        List<MasterDatum> roles = masterDatumService.getListByTypeName("ROLE");
        List<MasterDatum> status = masterDatumService.getListByTypeName("ACCOUNT STATUS");
        model.addAttribute("roles", roles);
        model.addAttribute("status", status);

        return "admin_side/EditUser"; // Sử dụng trang EditUser.html
    }
    @PostMapping("add-user")
    public String addUser(@ModelAttribute("user") AccountVo accountVo, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Received user data: " + accountVo);
            System.out.println("Role ID: " + accountVo.getRole());

            String randomPassword = UUID.randomUUID().toString();
            accountVo.setPassword(randomPassword);
            accountVo.setConfirmPassword(randomPassword);

            accountService.addUserFromAdmin(accountVo);
            redirectAttributes.addFlashAttribute("message", "User added successfully.");
            return "redirect:/admin/add-user";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/admin/add-user";
        }
    }

    // Display user detail
    @GetMapping("user-detail/{id}")
    public String userDetail(@PathVariable("id") Integer id, Model model) {
        AccountVo user = accountService.getAccountById(id);
        model.addAttribute("user", user);
        return "admin_side/UserDetails";
    }


    // Activate/Deactivate user account
    @PostMapping("user-detail/{id}/toggleStatus")
    public String toggleUserStatus(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            accountService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("message", "User status updated successfully.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
        }
        return "redirect:/admin/user-detail/" + id;
    }

    // Display Edit User
    @GetMapping("edit-user/{id}")
    public String showEditUserPage(@PathVariable("id") Integer id, Model model) {
        try {
            AccountVo user = accountService.getAccountById(id);
            List<MasterDatum> roles = masterDatumService.getListByTypeName("ROLE");
            // Lấy danh sách roles từ service
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "User not found.");
        }
        return "admin_side/EditUser";
    }

    // update User account
    @PostMapping("edit-user/{id}")
    public String updateUser(@ModelAttribute("user") AccountVo accountVo, RedirectAttributes redirectAttributes) {
        try {
            accountService.updateUser(accountVo);
            redirectAttributes.addFlashAttribute("message", "Change has been successfully updated.");
            return "redirect:/admin/edit-user/" + accountVo.getId();
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/admin/edit-user/" + accountVo.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/admin/edit-user/" + accountVo.getId();
        }
    }

}
