package fa.appcode.exceptions;

import lombok.Getter;

@Getter
public class EnrollUnenrollParentException extends RuntimeException {
    private int parentId;

    public EnrollUnenrollParentException(String message, int parentId) {
        super(message);
        this.parentId = parentId;
    }

    public EnrollUnenrollParentException(String message) {
        super(message);
    }

}
