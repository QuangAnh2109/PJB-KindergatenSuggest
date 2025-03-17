package fa.appcode.web.controller;

import com.cloudinary.provisioning.Account;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.Request;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.RequestService;
import fa.appcode.services.SchoolInfoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final MasterDatumService masterDatumService;
    private final AccountService accountService;
    private final SchoolInfoService schoolInfoService;
    private final GlobalConfig globalConfig;

    /**
     * Display request list
     *
     * @param currentPage current page number
     * @param model
     * @param principal   current logged-in user
     * @param session
     * @return view name "admin_side/request-list"
     */
    @GetMapping("/manager/request-list")
    public String showRequestList(@RequestParam(name = "currentPage", defaultValue = Constant.INIT_PAGE) int currentPage,
                                  Model model, Principal principal, HttpSession session) {
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(), Sort.by("fullName").ascending());
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? Constant.ADMIN_ROLE : Constant.SCHOOL_OWNER_ROLE;
        Page<RequestVo> requestList;
        if (role.equalsIgnoreCase(Constant.ADMIN_ROLE )) {
            requestList = requestService.listAllRequest(null,null,pageable);
        } else {
            int accountID = accountService.getAccountInfo(principal).getId();
            requestList = requestService.listAllRequest(accountID,null, pageable);
        }
        String message = (String) session.getAttribute("message");
        session.removeAttribute("message");

        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        model.addAttribute("message", message);
        model.addAttribute("role", role);
        return "admin_side/request-list";
    }

    /**
     * Display request reminders
     *
     * @param currentPage current page number
     * @param model
     * @param principal   current logged-in user
     * @param session
     * @return view name "admin_side/request-reminder"
     */
    @GetMapping("/manager/request-reminder")
    public String showRequestReminder(@RequestParam(name = "currentPage", defaultValue = Constant.INIT_PAGE) int currentPage,
                                      Model model, Principal principal, HttpSession session) {
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(), Sort.by("fullName").ascending());
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? Constant.ADMIN_ROLE : Constant.SCHOOL_OWNER_ROLE;
        Page<RequestVo> requestList;
        if (role.equalsIgnoreCase(Constant.ADMIN_ROLE)) {
            requestList = requestService.listAllRequest(null,2,pageable);
        } else {
            int accountID = accountService.getAccountInfo(principal).getId();
            requestList = requestService.listAllRequest(accountID,2, pageable);
        }
        String message = (String) session.getAttribute("message");
        session.removeAttribute("message");

        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        model.addAttribute("message", message);
        model.addAttribute("role", role);
        return "admin_side/request-reminder";
    }

    /**
     * Show request detail by request ID
     *
     * @param id          request ID
     * @param page        previous page
     * @param currentPage current page number
     * @param model
     * @param principal   current logged-in user
     * @return view name "admin_side/request-list-detail"
     */
    @GetMapping("/manager/request-list-detail")
    public String requestListDetail(@RequestParam Integer id,
                                    @RequestParam(name = "page", required = false) String page,
                                    @RequestParam(name = "currentPage", defaultValue = Constant.INIT_PAGE) int currentPage,
                                    Model model, Principal principal) {
        RequestDetailVo requestDetail = requestService.findById(id);
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? Constant.ADMIN_ROLE : Constant.SCHOOL_OWNER_ROLE;
        model.addAttribute("requestDetail", requestDetail);
        model.addAttribute("page", page);
        model.addAttribute("role", role);
        model.addAttribute("currentPage", currentPage);
        return "admin_side/request-list-detail";
    }

    /**
     * Update the request status based on the user's role.
     *
     * @param id                 the request ID
     * @param page               the previous page
     * @param currentPage        the current page number
     * @param model
     * @param principal          the current-loged user
     * @param redirectAttributes
     * @param session
     * @return the redirection path
     */
    @GetMapping("/manager/updateRequest")
    public String updateRequest(@RequestParam Integer id,
                                @RequestParam String page,
                                @RequestParam(name = "currentPage", defaultValue = Constant.INIT_PAGE) int currentPage,
                                Model model, Principal principal,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? Constant.ADMIN_ROLE : Constant.SCHOOL_OWNER_ROLE;
        if (role.equalsIgnoreCase(Constant.ADMIN_ROLE)) {
            requestService.updateRequest("SYSTEM_ADMIN", id);
        } else if (role.equalsIgnoreCase("School owner")) {
            requestService.updateRequest("SCHOOL_OWNER", id);
        }

        RequestDetailVo requestDetail = requestService.findById(id);
        model.addAttribute("requestDetail", requestDetail);
        model.addAttribute("role", role);
        redirectAttributes.addAttribute("currentPage", currentPage);
        session.setAttribute("message", "Update successfully!");
        if (page.equals("Detail")) return "redirect:/manager/request-reminder";
        return "redirect:/manager/request-list";
    }

    /**
     * Search for requests base on keyword
     *
     * @param currentPage the current page number
     * @param keyword     the search keyword
     * @param model
     * @param principal   the currently logged-in user
     * @return a ResponseEntity containing a page of RequestVo
     */
    @GetMapping("/manager/searchRequestList")
    public ResponseEntity<Page<RequestVo>> searchRequestList(@RequestParam(name = "currentPage", defaultValue = Constant.INIT_PAGE) int currentPage,
                                                             @RequestParam(name = "keyword", required = false) String keyword,
                                                             Model model, Principal principal) {
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(), Sort.by("fullName").ascending());
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? Constant.ADMIN_ROLE : Constant.SCHOOL_OWNER_ROLE;
        Page<RequestVo> requestList;
        if (role.equalsIgnoreCase(Constant.ADMIN_ROLE)) {
            requestList = requestService.searchRequest(keyword,null,null, pageable);
        } else {
            int accountID = accountService.getAccountInfo(principal).getId();
            requestList = requestService.searchRequest(keyword, accountID,null, pageable);
        }
        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        model.addAttribute("role", role);
        return ResponseEntity.ok(requestList);
    }

    /**
     * Search for unresolved request reminders based on keyword
     *
     * @param currentPage the current page number
     * @param keyword     the search keyword
     * @param model
     * @param principal   the currently logged-in user
     * @return a ResponseEntity containing a page of RequestVo
     */
    @GetMapping("/manager/searchRequestReminder")
    public ResponseEntity<Page<RequestVo>> searchRequestReminder(@RequestParam(name = "currentPage", defaultValue = Constant.INIT_PAGE) int currentPage,
                                                                 @RequestParam(name = "keyword", required = false) String keyword,
                                                                 Model model, Principal principal) {
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage(), Sort.by("fullName").ascending());
        String role = accountService.getAccountInfo(principal).getRoleId() == 1 ? Constant.ADMIN_ROLE : Constant.SCHOOL_OWNER_ROLE;
        Page<RequestVo> requestList;
        if (role.equalsIgnoreCase(Constant.ADMIN_ROLE)) {
            requestList = requestService.searchRequest(keyword,null,2, pageable);
        } else {
            int accountID = accountService.getAccountInfo(principal).getId();
            requestList = requestService.searchRequest(keyword, accountID,2, pageable);
        }
        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        model.addAttribute("role", role);
        return ResponseEntity.ok(requestList);
    }

    /**
     * Create a new counseling request.
     *
     * @param fullName           the full name of the requester
     * @param email              the email of the requester
     * @param phone              the phone number of the requester
     * @param inquiries          the inquiries or questions from the requester
     * @param principal          the currently logged-in user
     * @return the redirection path to "/public/search"
     */
    @PostMapping("/public/createRequest")
    public String createRequestCounseling(@RequestParam String fullName,
                                          @RequestParam String email,
                                          @RequestParam String phone,
                                          @RequestParam String inquiries,
                                          Principal principal){
        AccountInfo accountID = accountService.getAccountInfo(principal);
        SchoolInfo school = schoolInfoService.getSchoolInfoById(1);
        Request request = new Request(accountID,school,fullName,email,phone,inquiries,1,1,"PARENT",Instant.now());
        requestService.createRequest(request);
        return "redirect:/public/search";
    }
}
