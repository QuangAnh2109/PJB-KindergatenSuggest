package fa.appcode.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Placeholder {
    USER_NAME(":[-USER_NAME-]:"),
    TITLE(":[-TITLE-]:"),
    LINK(":[-LINK-]:"),
    EMAIL(":[-EMAIL-]:"),
    PASSWORD(":[-PASSWORD-]:"),
    OWNER_ACCOUNT(":[-OWNER_ACCOUNT-]:"),
    SCHOOL_NAME(":[-SCHOOL_NAME-]:");

    private final String placeholder;
}
