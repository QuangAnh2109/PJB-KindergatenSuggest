package fa.appcode.web.controller;


import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import fa.appcode.services.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import fa.appcode.common.utils.Constant;
import java.util.List;

@Controller
public class MyRequestController {
    private RequestService requestService;

    @GetMapping("/parent/my-request")
    public String myRequest(@SessionAttribute(name = "id", required = true)Integer id,
                            Model model,
                            @RequestParam(defaultValue = Constant.INIT_PAGE) int page,
                            @RequestParam(defaultValue = Constant.PAGE_SIZE) int page_size) {
//        Page<Request> listRequest = requestService.findRequestByAccountIdAndDeleteFlg(id);
//        model.addAttribute("listRequest", listRequest);
        return "user_side/my-request";
    }
}
