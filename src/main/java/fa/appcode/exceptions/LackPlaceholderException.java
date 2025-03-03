package fa.appcode.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LackPlaceholderException extends Exception {
    public LackPlaceholderException(String message) {
        super(message);
    }
}
