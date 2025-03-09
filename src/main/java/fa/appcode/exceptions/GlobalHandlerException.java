package fa.appcode.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalHandlerException {


    private static final Logger logger = LoggerFactory.getLogger(GlobalHandlerException.class);

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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
    }


}
