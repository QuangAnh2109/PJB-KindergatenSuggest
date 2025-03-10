package fa.appcode.exceptions;
import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {

    private static final Logger logger = Log4jUtils.getLogger(GlobalHandlerException.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        logger.error("Unhandled exception occurred", ex);
        model.addAttribute("globalError", "An unexpected error occurred. Please try again later.");
        return Constant.ERROR_PAGE;
    }
}
