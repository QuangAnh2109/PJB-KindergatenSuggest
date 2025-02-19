package fa.appcode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class ParentController {

    @GetMapping("/admin/parent-list/parent-details")
    public String parent_details() {
        return "admin_side/parent-details";
    }

    @GetMapping("/admin/parent-list")

    public String parent_list(Model model) {
        model.addAttribute("id",1);
        return "admin_side/parent-list";
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
