package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.MasterDatum;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDatumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin/")
public class UserManagementController {

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MasterDatumService masterDatumService;

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
                              Model model, Principal principal) throws Exception {

        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage());
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? "Admin" : "School owner";

        Page<AccountVo> accounts = accountService.getAllAccounts(search, pageable);
        List<AccountVo> listAccount = accounts.getContent();
        model.addAttribute("listAccounts", listAccount);
        model.addAttribute("searchField", search);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", accounts.getTotalPages());
        model.addAttribute("role", role);

        return "admin_side/user-list";
    }

    /**
     * Edit or Add user account Screen
     *
     * @param id
     * @param principal
     * @param model
     * @return
     */
    @GetMapping({"add-user", "edit-user/{id}"})
    public String showUserForm(@PathVariable(name = "id", required = false) Integer id, Model model, Principal principal) {
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? "Admin" : "School owner";

        AccountVo user = (id != null) ? accountService.getAccountById(id) : new AccountVo();

        List<MasterDatum> roles = masterDatumService.getListByTypeName("ROLE");
        List<MasterDatum> status = masterDatumService.getListByTypeName("ACCOUNT STATUS");

        model.addAttribute("role", role);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("status", status);

        return Constant.VIEW_ACCOUNT_PAGE;
    }


}
