package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@RequiredArgsConstructor

@Controller
public class LoginController {
    private final GlobalConfig globalConfig;
    @RequestMapping("/public/showMyLoginPage")
    public String showMyLoginPage(HttpServletRequest request, Model model,
                                  @RequestParam(value = "logout", required = false) String logout) {
        HttpSession session = request.getSession();
        Object error = session.getAttribute("loginError");
        if (error != null) {
            model.addAttribute("error", error);
            session.removeAttribute("loginError");
        }
        String email="email";
        Object emailInput = session.getAttribute(email);
        if (emailInput != null) {
            model.addAttribute(email, emailInput);
            session.removeAttribute(email);
        }
        if (logout != null) {
            model.addAttribute("message", globalConfig.getLogoutSuccessFully());
        }
        return "/user_side/login";
    }
    @GetMapping("/public/access-denied")
    public String accessDenied() {
        return Constant.ACCESS_DENIED_PAGE;
    }
}
