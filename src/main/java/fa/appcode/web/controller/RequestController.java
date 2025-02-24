package fa.appcode.web.controller;

import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.RequestService;
import fa.appcode.services.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String showRequestList(Model model) {
        List<Request> listRequest = requestService.findAll();
        List<RequestVo> requestList = new ArrayList<>();
        for (Request request : listRequest) {
            RequestVo requestVo = getRequestVo(request);
            requestList.add(requestVo);
        }
        model.addAttribute("requestList", requestList);
        return "admin_side/request-list";
    }
    @GetMapping("/school-owner/request-list-detail")
    public String request_list_detail(@RequestParam Integer id, Model model) {
        Request request = requestService.findById(id);

        RequestVo requestVo = getRequestVo(request);

        model.addAttribute("request", request);
        model.addAttribute("requestVo", requestVo);
        return "admin_side/request-list-detail";
    }
    @GetMapping("/school-owner/request-reminder")
    public String showRequestReminder(Model model) {
        List<Request> listRequest = requestService.findOpenedRequest();
        List<RequestVo> requestList = new ArrayList<>();
        for (Request request : listRequest) {
            RequestVo requestVo = getRequestVo(request);
            requestList.add(requestVo);
        }
        model.addAttribute("requestList", requestList);
        return "admin_side/request-reminder";
    }
}
