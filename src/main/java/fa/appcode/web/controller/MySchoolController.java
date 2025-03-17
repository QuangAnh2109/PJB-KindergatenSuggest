package fa.appcode.web.controller;

import ch.qos.logback.core.model.Model;
import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
@AllArgsConstructor
@Controller
public class MySchoolController {

    private final GlobalConfig globalConfig;

    @GetMapping("/parent/my-school")
    public String mySchool(@SessionAttribute(name = "idAccount", required = true) Integer id, Model model,@RequestParam(defaultValue = Constant.INIT_PAGE) int currentPage) {
//      Pageable pageable = PageRequest.of(currentPage,globalConfig.getSizeOfPage());

        return "user_side/my-school";
    }
}
