package fa.appcode.services;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.entities.AccountInfo;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Map;

public interface ValidateService {
    boolean requiredField(String fieldName);


    boolean validEmail(String email);

    boolean validPassword(String password);

    boolean validPhoneNumber(String phoneNumber);

    boolean checkDuplicateEmail(String email);

    boolean checkDuplicatePhone(String phoneNumber);


//    Map<String,String> validateChangePassword(String oldPassword, String newPassword, String confirmPassword);

    Map<String, String> validateChangePasswordRequired(String oldPassword, String newPassword, String confirmPassword);

    Map<String, String> validateValidChangePasswordFormats(String oldPassword, String newPassword, String confirmPassword);

    boolean checkPasswordsMatch(String password, String confirmPassword);

    boolean checkOldPassword(String password);

    Map<String, String> validatePasswordChangeRules(String oldPassword, String newPassword, String confirmPassword);

    boolean validateForgotPassword(String email, Model model);

    boolean validateResetPasswordRequired(String newPassword, String confirmPassword, Model model);

    boolean validateValidResetPasswordFormats(String newPassword, String confirmPassword, Model model);

    boolean validateResetPassword(String password, String confirmPassword, Model model);

    void validatePasswordResetRules(String password, String confirmPassword, Model model);

    boolean validateUpdateAccountReq(AccountInfo accountInfo, Model model);

    boolean validateUpdateAccountValid(AccountInfo accountInfo, Model model);

    boolean validateUpdateAccountDuplicate(String currentPhone, String newPhone, Model model);

    boolean validateUpdateAccount(AccountInfo accountInfo, String currentPhone, Model model);

    Map<String, String> validateRegisterRequired(AccountVo accountVo);

    Map<String, Object> validateRegistration(AccountVo accountVo);

    Map<String, String> validateForgotPassword(String email);

    Map<String, String> validateResetPassword(String newPassword, String confirmPassword);

    void validateReset(String newPassword, String confirmPassword);
    Map<String, String> validateAccountField(String fullName, String currentPhone, String newPhone, LocalDate dob);
}
