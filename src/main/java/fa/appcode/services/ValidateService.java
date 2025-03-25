package fa.appcode.services;


import fa.appcode.common.vo.AccountVo;

import java.time.LocalDate;
import java.util.Map;

public interface ValidateService {
    boolean requiredField(String fieldName);

    boolean validEmail(String email);

    boolean validPassword(String password);

    boolean validPhoneNumber(String phoneNumber);
    Map<String,String> dobValidation(LocalDate dob);

    Map<String, String> validateChangePasswordRequired(String oldPassword, String newPassword, String confirmPassword);

    Map<String, String> validateValidChangePasswordFormats(String oldPassword, String newPassword, String confirmPassword);

    boolean checkPasswordsMatch(String password, String confirmPassword);

    boolean checkOldPassword(String password);

    Map<String, String> validatePasswordChangeRules(String oldPassword, String newPassword, String confirmPassword);

    Map<String, String> validateForgotPassword(String email);
    Map<String, String> emailValidation(String email);
    Map<String, String> validateResetPassword(String newPassword, String confirmPassword);

    Map<String, String> validateAccountField(String fullName, String currentPhone, String newPhone, LocalDate dob);

    Map<String, String> registerValidation(String fullName, String email, String phone, String password, String confirmPassword);
    Map<String, String> validateAccountVo(AccountVo accountVo);
    String validateSearch(String search);
}
