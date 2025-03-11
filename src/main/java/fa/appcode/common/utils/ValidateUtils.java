package fa.appcode.common.utils;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidateUtils {
    private ValidateUtils() {
    }

    public static boolean validatePhone(String phone) {
        if (phone == null) {
            return false;
        }
        return Pattern.matches(Constant.PHONE_REGEX, phone);
    }

    public static boolean validatePass(String password) {
        if (password == null) {
            return false;
        }
        return Pattern.matches(Constant.PASSWORD_REGEX, password);
    }

}
