package fa.appcode.common.utils;

import java.math.BigDecimal;
import java.util.Map;
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

    public static void validateString(String value, String regex, String key, String msg1, String msg2, Map<String, String> errors) {
        if(validateString(value, Constant.NULL_REGEX, key, msg1, errors)) {
            validateString(value, regex, key, msg2, errors);
        }
    }

    public static boolean validateString(String value, String regex, String key, String msg, Map<String, String> errors) {
        if(!Pattern.matches(regex, value)) {
            errors.put(key, msg);
            return false;
        }
        return true;
    }

    public static void validateFee(BigDecimal value, String key, Map<String, String> errors, String... msg) {
        if(value == null) {
            errors.put(key, msg[0]);
        }
        else if(value.compareTo(new BigDecimal("10000000000")) > 0) {
            errors.put(key, msg[1]);
        }
        else if(value.compareTo(new BigDecimal("0")) <= 0){
            errors.put(key, msg[2]);
        }
    }

    public static void validateObjectNull(Object ob, String key, String msg, Map<String, String> errors) {
        if(ob == null) {
            errors.put(key, msg);
        }
    }
}
