package fa.appcode.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import fa.appcode.common.utils.Constant;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.sql.SQLException;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {


    private final Logger logger = LoggerFactory.getLogger(GlobalHandlerException.class);

    @ExceptionHandler(CustomDataException.class)
    public ResponseEntity<String> handleCustomDataException(CustomDataException ex) {
        logger.error("Database failed: {}" ,ex.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database Failed: " + ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        logger.error("Query data failed: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Query data failed: " + e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException e) {
        logger.error("SQL Failed: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SQL Failed:  " + e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        logger.error("Entity not found: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(ValidationException e) {
        logger.error("validate exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(NoResourceFoundException e) {
        logger.error("NoResourceFoundException occurred: {}", e.getMessage(), e);
        return Constant.ERROR_PAGE;
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenException(TokenException e) {
        logger.error("Token error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token error: " + e.getMessage());
    }

    @ExceptionHandler(FromToDateException.class)
    public ResponseEntity<String> handleInvalidDateException(FromToDateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(ValidateParentException.class)
    public String handleInvalidParentIdException(ValidateParentException e,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        redirectAttributes.addFlashAttribute("alertType", Constant.DANGER);
        return "redirect:" + Constant.PARENT_LIST_URL;
    }

    @ExceptionHandler(EnrollUnenrollParentException.class)
    public String handleValidateEnrollUnenrollParent(EnrollUnenrollParentException e,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        redirectAttributes.addFlashAttribute("alertType", Constant.DANGER);
        return "redirect:" + Constant.VIEW_PARENT_DETAIL_URL + e.getParentId();
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e) {
        logger.error("Undefined Error occurred: {}", e.getMessage(), e);
        return Constant.ERROR_PAGE;
    }
}
