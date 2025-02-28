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
    @GetMapping("userlist")
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


    @GetMapping("adduser")
    public String admin_addUser() {
        return "admin_side/AddUser";
    }


    // Display user detail
    @GetMapping("userdetail/{id}")
    public String userDetail(@PathVariable("id") Integer id, Model model) {
        AccountVo user = accountService.getAccountById(id);
        model.addAttribute("user", user);
        return "admin_side/UserDetails";
    }


    // Activate/Deactivate user account
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

    // Display Edit User
    @GetMapping("edituser/{id}")
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
    @PostMapping("/edituser/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @RequestParam String fullName,
                             @RequestParam String phone,
                             @RequestParam String dob,
                             @RequestParam Integer roleId,
                             RedirectAttributes redirectAttributes) {

        System.out.println("Received request to update user with ID: " + id);

        try {
            accountService.updateUser(id, fullName, phone, dob, roleId);
            redirectAttributes.addFlashAttribute("message", "Change has been successfully updated.");
            return "redirect:/admin/edituser/" + id;
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/admin/edituser/" + id;
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid role ID.");
            return "redirect:/admin/edituser/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/admin/edituser/" + id;
        }
    }

}
