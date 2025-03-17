package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.config.GlobalConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final GlobalConfig globalConfig;
    @GetMapping("/public/showMyLoginPage")
    public String showMyLoginPage(HttpServletRequest request, Model model,
                                  @RequestParam(value = "logout", required = false) String logout) {
        HttpSession session = request.getSession();
        Object error = session.getAttribute("loginError");

        if (error != null) {
            model.addAttribute("error", error);
            session.removeAttribute("loginError");
        } else if (logout != null) {
            model.addAttribute("message", globalConfig.getLogoutSuccessFully());
        }

        Object emailInput = session.getAttribute("email");
        if (emailInput != null) {
            model.addAttribute("email", emailInput);
            session.removeAttribute("email");
        }

        return "/user_side/login";
    }
    @GetMapping("/public/access-denied")
    public String accessDenied() {
        return Constant.ACCESS_DENIED_PAGE;
    }
}
