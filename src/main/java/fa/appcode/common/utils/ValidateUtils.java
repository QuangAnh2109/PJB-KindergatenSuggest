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

    public static boolean isValidEmail(String email) {
        if(email == null) {
            return false;
        }
        return Pattern.matches(Constant.EMAIL_REGEX, email);
    }

    public static boolean isValidFullName(String fullName) {
        return fullName != null && !fullName.trim().isEmpty();
    }
    public static boolean isValidDob(LocalDate dob) {
        return dob != null && dob.isBefore(LocalDate.now());
    }

    public static boolean isValidRole(String role) {
        return role != null && !role.trim().isEmpty();
    }
    public static boolean isValidStatus(String status) {
        return status != null && !status.trim().isEmpty();
    }
}
