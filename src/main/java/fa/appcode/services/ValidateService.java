package fa.appcode.services;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.entities.AccountInfo;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Map;

public interface ValidateService {
    boolean requiredField(String fieldName);


    boolean validEmail(String email);

    boolean validPassword(String password);

    boolean validPhoneNumber(String phoneNumber);

    boolean checkDuplicateEmail(String email);

    boolean checkDuplicatePhone(String phoneNumber);

    Map<String, Object> validateRegistration(AccountVo accountVo, BindingResult bindingResult, Model model);

    boolean validateRegisterRequired(AccountVo accountVo, Model model);

    boolean validateValidRegister(AccountVo accountVo, Model model);

    void validateDuplicateRegister(AccountVo accountVo, Model model);

    Map<String, Object> validateChangePassword(String oldPassword, String newPassword, String confirmPassword, Model model);

    boolean validateChangePasswordRequired(String oldPassword, String newPassword, String confirmPassword, Model model);

    boolean validateValidChangePasswordFormats(String oldPassword, String newPassword, String confirmPassword, Model model);

    boolean checkPasswordsMatch(String password, String confirmPassword);

    boolean checkOldPassword(String password);

    void validatePasswordChangeRules(String oldPassword, String newPassword, String confirmPassword, Model model);

    boolean validateForgotPassword(String email, Model model);

    boolean validateResetPasswordRequired(String newPassword, String confirmPassword, Model model);

    boolean validateValidResetPasswordFormats(String newPassword, String confirmPassword, Model model);

    boolean validateResetPassword(String password, String confirmPassword, Model model);

    void validatePasswordResetRules(String password, String confirmPassword, Model model);

    boolean validateUpdateAccountReq(AccountInfo accountInfo, Model model);

    boolean validateUpdateAccountValid(AccountInfo accountInfo, Model model);

    boolean  validateUpdateAccountDuplicate(String currentPhone, String newPhone, Model model);

    boolean validateUpdateAccount(AccountInfo accountInfo,String currentPhone, Model model);

}
