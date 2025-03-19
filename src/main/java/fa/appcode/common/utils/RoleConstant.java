package fa.appcode.common.utils;

import java.util.Map;

public class RoleConstant {
    public static final String ADMIN = "USER_ADMIN";
    public static final String SCHOOL_OWNER = "USER_SCHOOL_OWNER";
    public static final String PARENT = "USER_PARENT";
    public static final String GUEST = "USER_GUEST";

    public static final Map.Entry<Integer, String> ADMIN_ROLE = Map.entry(1, "Admin");
    public static final Map.Entry<Integer, String> SCHOOL_OWNER_ROLE = Map.entry(2, "School owner");
    public static final Map.Entry<Integer, String> PARENT_ROLE = Map.entry(3, "Parent");

    public static final String FROM_BUTTON_ADMIN = "admin";
    public static final String FROM_BUTTON_OWNER = "owner";
}
