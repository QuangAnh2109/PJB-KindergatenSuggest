package fa.appcode.web.controller;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/")
public class ParentController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private AccountRepository accountRepository;
    @GetMapping("/admin/parent-list/parent-details/{id}")
    public String parent_details(@RequestParam(name = "currentPage"
            ,defaultValue = "0") int currentPage,@PathVariable("id") int id,Model model) {
        Pageable pageable = PageRequest.of(currentPage, 10,
                Sort.by("id").ascending());
        ParentVo accountInfo = accountService.findParentById(id);
        Page<EnrolledSchoolVo> listParentEnroll = accountService.findEnrolledSchoolBy(id,pageable);
        List<EnrolledSchoolVo> enrolledSchools = listParentEnroll.getContent();
        model.addAttribute("enrolledSchools", enrolledSchools);
        model.addAttribute("accountInfo", accountInfo);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", listParentEnroll.getTotalPages());
        return "admin_side/parent-details";
    }

//    @GetMapping("/admin/parent-list")
//
//    public String parent_list(Model model) {
//        model.addAttribute("id",1);
//        return "admin_side/parent-list";
//    }

    @GetMapping("/admin/parent-list")
    public String test(@RequestParam(name = "currentPage"
              ,defaultValue = "0") int currentPage, Model model) {
        Pageable pageable = PageRequest.of(currentPage, 5,
                  Sort.by("id").ascending());
        Page<ParentVo> list = accountService.findAllParent(pageable);
        List<ParentVo> accounts = list.getContent();
//        List<AccountInfo> roles = accountService.findAllRoles();
        model.addAttribute("accounts", accounts);
//        model.addAttribute("roles", roles)
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", list.getTotalPages());
        return"admin_side/parent-list";
    }
//    @GetMapping("/test")
//
//    public AccountVo getAccount(@PathVariable Integer id) {
//        AccountInfo account = accountService.findAll(id);
//        return new AccountVo(account); // Prevents lazy-loading issues
//    }
//    @GetMapping("/test")
//    public String showMasterList(@RequestParam(name = "currentPage"
//            ,defaultValue = "1") int currentPage, Model model) throws Exception {
//        Pageable pageable = PageRequest.of(currentPage, 10,
//                Sort.by("fullName").descending());
//        Page<AccountInfo> list = accountService.findAll(pageable);
//        List<AccountInfo> masters = list.getContent();
//
//        System.out.println(masters.get(0).getId());
//        model.addAttribute("dude", masters);
//        return "admin_side/parent-list";
//    }
}
