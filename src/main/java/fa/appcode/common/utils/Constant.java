package fa.appcode.common.utils;


public class Constant {

    ///  USER SIDE
    public static final String USER_INIT_PAGE = "0";
    public static final String SCHOOL_AND_ENROLL_INIT_PAGE = "0";
    public static final String INIT_PAGE = "0";
    public static final String PAGE_SIZE = "10";
    public static final String KEY_WORD_DEFAULT = "";
    public static final String VIEW_ACCOUNT_PAGE = "admin_side/edit-account";
    public static final String FORGOT_PASSWORD_PAGE = "user_side/forgot-password";
    public static final String RESET_PASSWORD_PAGE = "user_side/reset-password";
    public static final String TOKEN_INVALID_PAGE = "user_side/token-invalid";
    public static final String CHANGE_PASSWORD_PAGE = "user_side/change-password";
    public static final String LOGIN_PAGE = "user_side/login";
    public static final String REGISTER_PAGE = "user_side/register";
    public static final String VERIFY_ACCOUNT_PAGE = "user_side/verify-account";
    public static final String ACCESS_DENIED_PAGE = "user_side/access-denied";
    //URL
    public static final String VIEW_ACCOUNT_URL = "/auth/view-account";
    public static final String REGISTER_VERIFY_URL = "http://localhost:8080/public/register/verify?token=";
    public static final String VIEW_PARENT_DETAIL_URL = "/manager/parent-list/parent-details/";
    private static final String CHANGE_PASSWORD_URL = "/auth/change-password";
    private static final String HOME_URL = "/public/home";
    private static final String MANAGER_REQUEST_LIST_URL = "/manager/request-list";
    public static final String RESET_PASSWORD_URL = "http://localhost:8080/public/reset-password?token=";
    public static final String PARENT_LIST_URL = "/manager/parent-list";

    //ADMIN SIDE

    //REGEX
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$";
    public static final String HOME_PAGE = "user_side/index";
    public static final String HOME_PAGE_URL = "public/home";
    public static final String EMAIL_REGEX_HTML = "\\w[\\w0-9]*@gmail.com";
    public static final String PHONE_REGEX_HTML = "/(84|0)[0-9]{9}/g";
    public static final String EMAIL_REGEX = "^[a-z][a-z0-9]*@gmail.com$";
    public static final String PHONE_REGEX = "^\\+?[0-9]{10,12}$";
    //CONSTANT VALUE
    public static final Integer PARENT_ROLE_ID = 3;
    public static final Integer SCHOOL_OWNER_ID = 2;
    public static final Integer ADMIN_ROLE_ID = 1;
    public static final String PARENT_ROLE = "Parent";
    public static final String SCHOOL_OWNER_ROLE = "School owner";
    public static final String ADMIN_ROLE = "Admin";
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_INACTIVE = 0;
    public static final Integer STATUS_DELETED = 0;
    public static final String WEB_SYSTEM = "WEB_SYSTEM";
    public static final int SEND_EMAIL_FORGOT = 1;
    public static final String INVALID_TOKEN_FORMAT = "Invalid token format";
    public static final String INVALID_TOKEN = "Invalid token structure";
    public static final String APPROVE_ENROLL_REQUEST ="approve";
    public static final String REJECT_ENROLL_REQUEST ="reject";
    public static final String UNENROLL_PARENT_SCHOOL ="unenroll";
    public static final String ENROLL_PARENT_SCHOOL ="enroll";
    public static final Integer SCHOOL_PUBLISH_STATUS = 5;

    public static final String ADMIN = "USER_ADMIN";
    public static final String SCHOOL_OWNER = "USER_SCHOOL_OWNER";
    public static final String PARENT = "USER_PARENT";
    public static final String GUEST = "USER_GUEST";

    public static final int SCHOOL_STATUS_SAVED_ID = 1;
    public static final int SCHOOL_STATUS_SUBMITTED_ID = 2;
    public static final int SCHOOL_STATUS_APPROVED_ID = 3;
    public static final int SCHOOL_STATUS_REJECTED_ID = 4;
    public static final int SCHOOL_STATUS_PUBLISHED_ID = 5;
    public static final int SCHOOL_STATUS_UNPUBLISHED_ID = 6;
    public static final int SCHOOL_STATUS_DELETED_ID = 7;

    public static final String SUCCESS = "success";
    public static final String DANGER = "danger";
    public static final int ENROLL_STATUS_ENROLL=3;
    public static final int ENROLL_STATUS_UNENROLL=4;

}
