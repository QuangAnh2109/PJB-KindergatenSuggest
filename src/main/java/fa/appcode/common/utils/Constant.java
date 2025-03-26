package fa.appcode.common.utils;


public class Constant {

    ///  USER SIDE
    public static final String USER_INIT_PAGE = "0";
    public static final String SCHOOL_AND_ENROLL_INIT_PAGE = "0";
    public static final String INIT_PAGE = "0";
    public static final String PAGE_SIZE = "10";
    public static final String KEY_WORD_DEFAULT = "";
    public static final String VIEW_ACCOUNT_PAGE = "user_side/view-account";
    public static final String ACCOUNT_MANAGEMENT_PAGE = "user_side/view-account";

    public static final String FORGOT_PASSWORD_PAGE = "user_side/forgot-password";
    public static final String RESET_PASSWORD_PAGE = "user_side/reset-password";
    public static final String TOKEN_INVALID_PAGE = "user_side/token-invalid";
    public static final String CHANGE_PASSWORD_PAGE = "user_side/change-password";
    public static final String LOGIN_PAGE = "user_side/login";
    public static final String REGISTER_PAGE = "user_side/register";
    public static final String VERIFY_ACCOUNT_PAGE = "user_side/verify-account";
    public static final String ACCESS_DENIED_PAGE = "user_side/access-denied";
    public static final String ERROR_PAGE = "user_side/error";

    //URL
    public static final String VIEW_ACCOUNT_URL = "/auth/view-account";
    public static final String REGISTER_VERIFY_URL = "http://localhost:8080/public/register/verify?token=";
    public static final String VIEW_PARENT_DETAIL_URL = "/manager/parent-list/parent-details/";
    private static final String CHANGE_PASSWORD_URL = "/auth/change-password";
    private static final String HOME_URL = "/public/home";
    private static final String MANAGER_REQUEST_LIST_URL = "/manager/request-list";
    public static final String RESET_PASSWORD_URL = "http://localhost:8080/public/reset-password?token=";
    public static final String PARENT_LIST_URL = "/manager/parent-list";
    public static final String REQUEST_REMINDER_URL = "http://localhost:8080/manager/request-reminder";

    //ADMIN SIDE
    public static final String SCHOOL_FEEDBACK_RATING_MANAGER_PAGE = "admin_side/school-feedback-rating";
    public static final String SCHOOL_CREATE_PAGE = "admin_side/school-form";
    public static final String SCHOOL_DETAIL_MANAGER_PAGE = "admin_side/school-detail";
    public static final String SCHOOL_LIST_MANAGER_PAGE = "admin_side/school-list-manager";
    public static final String PARENT_DETAIL_PAGE = "admin_side/parent-details";
    public static final String PARENT_LIST_PAGE = "admin_side/parent-list";
    public static final String USER_LIST_PAGE = "admin_side/user-list";
    public static final String USER_DETAIL_PAGE = "admin_side/edit-account";
    public static final String REQUEST_LIST_URL = "admin_side/request-list";
    public static final String REQUEST_REMINDER_URL_HTML = "admin_side/request-reminder";
    public static final String REQUEST_LIST_CONTROLLER = "/manager/request-list";
    public static final String SEARCH_REQUEST_LIST_CONTROLLER = "/manager/searchRequestList";
    //REGEX
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,72}$";
    public static final String HOME_PAGE = "user_side/index";
    public static final String HOME_PAGE_URL = "/home";
    public static final String EMAIL_REGEX_HTML = "\\w[\\w0-9]*@gmail.com";
    public static final String PHONE_REGEX_HTML = "/(84|0)[0-9]{9}/g";
    public static final String EMAIL_REGEX = "^[a-z][a-z0-9]*@gmail.com$";
    public static final String PHONE_REGEX = "^(?:\\+84|0)\\d{9}$\n";
    public static final String MAIL_REGEX = "^(?=.{1,255}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
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
    public static final int SEND_EMAIL_REGISTER = 1;
    public static final String REQUEST_LIST_PAGE = "requestList";
    public static final String REQUEST_REMINDER_PAGE = "requestReminder";
    public static final String message= "message";

    public static final int SEND_REQUEST_REMINDER = 7;
    public static final String INVALID_TOKEN_FORMAT = "Invalid token format";
    public static final String INVALID_TOKEN = "Invalid token structure";
    public static final String APPROVE_ENROLL_REQUEST ="approve";
    public static final String REJECT_ENROLL_REQUEST ="reject";
    public static final String UNENROLL_PARENT_SCHOOL ="unenroll";
    public static final String ENROLL_PARENT_SCHOOL ="enroll";
    public static final Integer SCHOOL_PUBLISH_STATUS = 5;
    public static final int PAGE_DEFAULT = 0;
    public static final String SEARCH_ALL = "";
    public static final String SUCCESS = "success";
    public static final String DANGER = "danger";
    public static final int ENROLL_STATUS_ENROLL=3;
    public static final int ENROLL_STATUS_UNENROLL=4;

    public static final String IMAGE_DIR = "src/main/resources/static/admin_side/images";
    public static final String alertType = "alertType";
    public static final String parentMessage ="message";
    public static final String parentCurrentPage ="currentPage";
    public static final String parentPageSize ="numberPage";

    public static final String ERROR_PROPERTIES_CODE = "Message not found";
}
