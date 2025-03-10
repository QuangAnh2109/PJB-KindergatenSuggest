package fa.appcode.web.controller;


import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.RequestService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import fa.appcode.common.utils.Constant;

@Controller
@AllArgsConstructor
public class MyRequestController {
    private final GlobalConfig globalConfig;
    private final RequestService requestService;

    private final Logger logger = LoggerFactory.getLogger(MyRequestController.class);

    @GetMapping("/parent/my-request")
    public String myRequest(@SessionAttribute(name = "idAccount",required = true)Integer id,
                            @RequestParam(defaultValue = Constant.INIT_PAGE) int currentPage,
                            Model model) {
        Pageable pageable = PageRequest.of(currentPage, globalConfig.getSizeOfPage());
        Page<RequestDetailVo> listRequest = requestService.findRequestByAccountId(id,pageable);
        model.addAttribute("requestList", listRequest);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", listRequest.getTotalPages());
        return "user_side/my-request";
    }
}
