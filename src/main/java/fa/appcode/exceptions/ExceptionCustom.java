package fa.appcode.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ExceptionCustom extends RuntimeException {
    @Getter
    Map<String,String> errors;
    public ExceptionCustom(Map<String,String> message) {
            errors = message;
    }
}
