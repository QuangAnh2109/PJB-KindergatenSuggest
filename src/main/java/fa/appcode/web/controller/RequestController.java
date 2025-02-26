package fa.appcode.web.controller;

import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.Request;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.RequestService;
import fa.appcode.services.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private MasterDatumService masterDatumService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private SchoolInfoService schoolInfoService;


    public String getUserName(Principal principal) {
        String user = principal.getName();
        AccountInfo account = accountService.findByEmail(user);
        return account.getRoleId() == 1 ?"Admin":"School owner";
    }
    public int getUserID(Principal principal) {
        String user = principal.getName();
        AccountInfo account = accountService.findByEmail(user);
        return account.getId();
    }
    @GetMapping("/manager/request-list")
    public String showRequestList(@RequestParam(name = "currentPage"
            ,defaultValue = "0") int currentPage, Model model,Principal principal,
            @RequestParam(name = "message", defaultValue = "") String message                     ) {
        Pageable pageable = PageRequest.of(currentPage, 5, Sort.by("id").ascending());
        String role = getUserName(principal);
        Page<RequestVo> requestList ;
        if(role.equalsIgnoreCase("Admin")) {
            requestList = requestService.findAll(pageable);
        }else {
            int accountID = getUserID(principal);
            requestList = requestService.listAllRequestWithSchoolOwner(accountID,pageable);
        }
        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        model.addAttribute("message", message);
        model.addAttribute("role", role);
        return "admin_side/request-list";
    }
    @GetMapping("/manager/request-reminder")
    public String showRequestReminder(@RequestParam(name = "currentPage"
                                              ,defaultValue = "0") int currentPage, Model model,
        @RequestParam(name = "message", defaultValue = "")String message,Principal principal) {
        Pageable pageable = PageRequest.of(currentPage, 5, Sort.by("id").ascending());
        String role = getUserName(principal);
        Page<RequestVo> requestList ;
        if(role.equalsIgnoreCase("Admin")) {
            requestList = requestService.findOpenedRequest(pageable);
        }else{
            int accountID = getUserID(principal);
            requestList = requestService.findOpenedRequestWithSchoolOwner(accountID,pageable);
        }
        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        model.addAttribute("message", message);
        model.addAttribute("role", role);
        return "admin_side/request-reminder";
    }
    @GetMapping("/manager/request-list-detail")
    public String requestListDetail(@RequestParam Integer id,@RequestParam(name = "page", required = false) String page, Model model,
                                    Principal principal) {
        RequestDetailVo requestDetail = requestService.findById(id);
        String role = getUserName(principal);
        model.addAttribute("requestDetail", requestDetail);
        model.addAttribute("page", page);
        model.addAttribute("role", role);
        return "admin_side/request-list-detail";
    }
    @GetMapping("/manager/updateRequest")
    public String updateRequest(@RequestParam Integer id,@RequestParam String page, Model model,Principal principal,
             RedirectAttributes redirectAttributes) {
        String role = getUserName(principal);
        if(role.equalsIgnoreCase("Admin")) {
            requestService.updateRequest("SYSTEM_ADMIN",id);
        }else if(role.equalsIgnoreCase("School owner")) {
            requestService.updateRequest("SCHOOL_OWNER",id);
        }
        RequestDetailVo requestDetail = requestService.findById(id);
        model.addAttribute("requestDetail", requestDetail);
        model.addAttribute("role", role);
        redirectAttributes.addAttribute("message", "Update successfully!");
        if(page.equals("Detail")) return "redirect:/manager/request-reminder";
        return "redirect:/manager/request-list";
    }

    @GetMapping("/manager/searchRequestList")
    public String searchRequestList(@RequestParam(name = "currentPage"
            ,defaultValue = "0") int currentPage,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model,Principal principal) {
        Pageable pageable = PageRequest.of(currentPage, 5, Sort.by("id").ascending());
        String role = getUserName(principal);
        Page<RequestVo> requestList ;
        if(role.equalsIgnoreCase("Admin")) {
            requestList = requestService.searchRequest(keyword,pageable);
        }else{
            int accountID = getUserID(principal);
            requestList = requestService.searchRequestWithSchoolOwner(keyword,accountID,pageable);
        }
        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        model.addAttribute("role", role);
        return "admin_side/request-list";
    }
    @GetMapping("/manager/searchRequestReminder")
    public String searchRequestReminder(@RequestParam(name = "currentPage"
                                            ,defaultValue = "0") int currentPage,
                                    @RequestParam(name = "keyword", required = false) String keyword,
                                    Model model,Principal principal) {
        Pageable pageable = PageRequest.of(currentPage, 5, Sort.by("id").ascending());
        String role = getUserName(principal);
        Page<RequestVo> requestList ;
        if(role.equalsIgnoreCase("Admin")) {
            requestList = requestService.searchRequestReminder(keyword,pageable);
        }else{
            int accountID = getUserID(principal);
            requestList = requestService.searchRequestReminderWithSchoolOwner(keyword,accountID,pageable);
        }
        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        model.addAttribute("role", role);
        return "admin_side/request-reminder";
    }
}
