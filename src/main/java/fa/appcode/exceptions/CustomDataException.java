package fa.appcode.exceptions;

public class CustomDataException extends  RuntimeException {
    public CustomDataException(String message) {
        super(message);
    }

    public CustomDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
