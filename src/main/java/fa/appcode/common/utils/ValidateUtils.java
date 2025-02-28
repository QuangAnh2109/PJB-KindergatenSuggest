package fa.appcode.common.utils;

import java.util.regex.Pattern;

public class ValidateUtils {
    private static final String PHONE_REGEX = "^\\+?[0-9]{10,12}$";

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$";

    public static boolean validatePhone(String phone) {
        if (phone == null) {
            return false;
        }
        return Pattern.matches(PHONE_REGEX, phone);
    }

    public static boolean validatePass(String password) {
        if (password == null) {
            return false;
        }
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
