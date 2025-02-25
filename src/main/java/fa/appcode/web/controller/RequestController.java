package fa.appcode.web.controller;

import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class RequestController {
    @Autowired
    private RequestService requestService;
    @Autowired
    private MasterDatumService masterDatumService;

    @Autowired
    private SchoolInfoService schoolInfoService;

    public RequestVo getRequestVo(Request request){
        Integer id = request.getId();
        String fullName = request.getFullName();
        String email = request.getRequestEmail();
        String phone = request.getRequestPhone();
        String requestMasterName = masterDatumService.findNameById(request.getRequestMasterId());
        return new RequestVo(id, fullName, email, phone, requestMasterName);
    }
    
    @GetMapping("/school-owner/request-list")
    public String showRequestList(@RequestParam(name = "currentPage"
            ,defaultValue = "0") int currentPage, Model model) {
        Pageable pageable = PageRequest.of(currentPage, 5, Sort.by("id").ascending());
        Page<RequestVo> requestList = requestService.findAll(pageable);
        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        return "admin_side/request-list";
    }
    @GetMapping("/school-owner/request-list-detail")
    public String request_list_detail(@RequestParam Integer id, Model model) {
        RequestDetailVo requestDetail = requestService.findById(id);
        model.addAttribute("requestDetail", requestDetail);
        return "admin_side/request-list-detail";
    }
    @GetMapping("/school-owner/request-reminder")
    public String showRequestReminder(@RequestParam(name = "currentPage"
            ,defaultValue = "0") int currentPage, Model model) {
        Pageable pageable = PageRequest.of(currentPage, 5, Sort.by("id").ascending());
        Page<RequestVo> requestList = requestService.findOpenedRequest(pageable);
        model.addAttribute("requestList", requestList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("numberPage", requestList.getTotalPages());
        return "admin_side/request-reminder";
    }
}
