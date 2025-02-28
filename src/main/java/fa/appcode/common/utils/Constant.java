package fa.appcode.common.utils;


public class Constant {

    ///  USER SIDE
    public static final String USER_INIT_PAGE = "0";
    public static final String SCHOOL_AND_ENROLL_INIT_PAGE = "0";
    public static final String INIT_PAGE = "0";
    public static final String KEY_WORD_DEFAULT = "";
    public static final String VIEW_ACCOUNT_PAGE = "user_side/view-account";
    public static final String FORGOT_PASSWORD_PAGE = "user_side/forgot-password";
    public static final String RESET_PASSWORD_PAGE = "user_side/reset-password";
    public static final String TOKEN_INVALID_PAGE = "user_side/token-invalid";
    public static final String WEB_SYSTEM = "WEB_SYSTEM";
    public static final String LOGIN_PAGE = "user_side/login";
    public static final String REGISTER_PAGE = "user_side/register";
    public static final String VERIFY_ACCOUNT_PAGE = "user_side/verify-account";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$";

    public static final String EMAIL_REGEX_HTML = "\\w[\\w0-9]*@gmail.com";
    public static final String PHONE_REGEX_HTML = "/(84|0)[0-9]{9}/g";

    //ADMIN SIDE
    public static final String EMAIL_REGEX = "^[a-z][a-z0-9]*@gmail.com$";
    public static final String PHONE_REGEX = "^(84|0)[0-9]{9}$";
    public static final String PARENT_ROLE = "3";
    public static final String STATUS_ACTIVE = "1";
}
