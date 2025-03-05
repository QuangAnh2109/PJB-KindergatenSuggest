package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.MasterDatum;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
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

import java.security.Principal;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private EmailService emailService;

    /**
     * Pagination, search, show list of movies
     *
     * @param search
     * @param currentPage
     * @param model
     * @return
     */
    @GetMapping("user-list")
    public String getUserList(@RequestParam(defaultValue = Constant.KEY_WORD_DEFAULT) String search,
                              @RequestParam(defaultValue = Constant.USER_INIT_PAGE) int currentPage,
                              Model model, Principal principal) {

        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage());
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? "Admin" : "School owner";

        Page<AccountVo> accounts = accountService.getAllAccounts(search, pageable);
        List<AccountVo> listAccount = accounts.getContent();
        model.addAttribute("listAccounts", listAccount);
        model.addAttribute("searchField", search);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", accounts.getTotalPages());
        model.addAttribute("role", role);

        return "admin_side/UserList";
    }

    /**
     * show Add User screen
     *
     * @param model
     * @return
     */
    @GetMapping("add-user")
    public String showAddUserPage(Model model) {
        model.addAttribute("user", new AccountVo()); // Gửi một user rỗng để form hiển thị đúng
        List<MasterDatum> roles = masterDatumService.getListByTypeName("ROLE");
        List<MasterDatum> status = masterDatumService.getListByTypeName("ACCOUNT STATUS");
        model.addAttribute("roles", roles);
        model.addAttribute("status", status);

        return "admin_side/EditUser"; // Sử dụng trang EditUser.html
    }

    /**
     * Adds a new user account
     *
     * @param accountVo
     * @param redirectAttributes
     * @return
     */

    @PostMapping("add-user")
    public String addUser(@ModelAttribute("user") AccountVo accountVo, RedirectAttributes redirectAttributes) {
        try {

            // Auto-generated password
            String randomPassword = UUID.randomUUID().toString();

            accountVo.setPassword(randomPassword);
            accountVo.setConfirmPassword(randomPassword);

            accountService.addUserFromAdmin(accountVo);  // Add


            //Send mail contains information to login
            emailService.sendEmailToMany(SendMailInfo.builder().toMail(List.of(accountVo.getEmail()))
                    .ccMail(List.of())
                    .mailId(2)
                    .detail(Map.of(Placeholder.USER_NAME, accountVo.getEmail(),
                            Placeholder.EMAIL, accountVo.getEmail(),
                            Placeholder.PASSWORD, randomPassword,
                            Placeholder.OWNER_ACCOUNT, "SYSTEM_ADMIN"))
                    .build());

            redirectAttributes.addFlashAttribute("message", "User added successfully.");
            return "redirect:/admin/add-user";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/admin/add-user";
        }
    }

    /**
     * show User detail screen
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("user-detail/{id}")
    public String userDetail(@PathVariable("id") Integer id, Model model) {
        AccountVo user = accountService.getAccountById(id);
        model.addAttribute("user", user);
        return "admin_side/UserDetails";
    }


    /**
     * changes Status of user account  (active <-> inactive)
     *
     * @param id
     * @param redirectAttributes
     * @return
     */
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

    /**
     * show Edit user account screen
     *
     * @param id
     * @param model
     * @return
     */
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

    /**
     * Update user account
     *
     * @param accountVo
     * @param redirectAttributes
     * @return
     */
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
