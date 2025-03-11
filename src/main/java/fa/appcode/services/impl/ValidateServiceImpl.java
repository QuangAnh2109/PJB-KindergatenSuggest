package fa.appcode.services.impl;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Implementation of the ValidateService for validating various inputs including
 * registration forms and password changes.
 */
@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
    private static final Logger LOGGER = Log4jUtils.getLogger(ValidateServiceImpl.class);
    private final GlobalConfig globalConfig;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // Constants for model attribute names
    static final String NEW_PASSWORD_ERROR = "newPasswordError";
    static final String EMAIL_ERROR = "emailError";
    static final String PHONE_ERROR = "phoneError";
    static final String PASSWORD_ERROR = "passwordError";
    static final String FULL_NAME_ERROR = "fullNameError";
    static final String CONFIRM_PASSWORD_ERROR = "confirmPasswordError";
    static final String FIELD_ERROR = "errorField";
    static final String IS_VALID = "isValid";

    /**
     * Checks if a field is required (null or empty)
     *
     * @param fieldName The field to check
     * @return true if the field is null or empty, false otherwise
     */
    @Override
    public boolean requiredField(String fieldName) {
        return fieldName == null || fieldName.trim().isEmpty();
    }

    /**
     * Validates if an email address matches the expected format
     *
     * @param email The email to validate
     * @return true if the email is valid, false otherwise
     */
    @Override
    public boolean validEmail(String email) {
        LOGGER.debug("Validating email: {}", email);
        return Pattern.matches(Constant.MAIL_REGEX, email);
    }

    /**
     * Validates if a password matches the expected format
     *
     * @param password The password to validate
     * @return true if the password is valid, false otherwise
     */
    @Override
    public boolean validPassword(String password) {
        LOGGER.debug("Validating password format");
        return Pattern.matches(Constant.PASSWORD_REGEX, password);
    }

    /**
     * Validates if a phone number matches the expected format
     *
     * @param phoneNumber The phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    @Override
    public boolean validPhoneNumber(String phoneNumber) {
        LOGGER.debug("Validating phone number: {}", phoneNumber);
        return Pattern.matches(Constant.PHONE_REGEX, phoneNumber);
    }

    /**
     * Checks if an email already exists in the database
     *
     * @param email The email to check
     * @return true if the email exists, false otherwise
     */
    @Override
    public boolean checkDuplicateEmail(String email) {
        LOGGER.debug("Checking for duplicate email: {}", email);
        boolean isDuplicate = accountRepository.findByEmail(email) != null;
        if (isDuplicate) {
            LOGGER.warn("Duplicate email detected: {}", email);
        }
        return isDuplicate;
    }

    /**
     * Checks if a phone number already exists in the database
     *
     * @param phoneNumber The phone number to check
     * @return true if the phone number exists, false otherwise
     */
    @Override
    public boolean checkDuplicatePhone(String phoneNumber) {
        LOGGER.debug("Checking for duplicate phone: {}", phoneNumber);
        boolean isDuplicate = accountRepository.findAccountByPhone(phoneNumber) != null;
        if (isDuplicate) {
            LOGGER.warn("Duplicate phone detected: {}", phoneNumber);
        }
        return isDuplicate;
    }

    /**
     * Validates a registration form
     *
     * @param accountVo     The account view object containing registration data
     * @param bindingResult The binding result containing validation errors
     * @param model         The model to add attribute errors to
     * @return A map containing validation results
     */
    @Override
    public Map<String, Object> validateRegistration(AccountVo accountVo, BindingResult bindingResult, Model model) {
        LOGGER.debug("Validating registration for email: {}", accountVo.getEmail());

        Map<String, Object> result = new HashMap<>();
        result.put(IS_VALID, true);

        if (bindingResult.hasErrors()) {
            LOGGER.warn("Form validation failed - email: {}", accountVo.getEmail());
            result.put(IS_VALID, false);
            result.put(FIELD_ERROR, "form");
            return result;
        }
        validateRegisterRequired(accountVo, model);
        validateValidRegister(accountVo, model);
        validateDuplicateRegister(accountVo, model);

        if (model.containsAttribute(FULL_NAME_ERROR) ||
                model.containsAttribute(EMAIL_ERROR) ||
                model.containsAttribute(PHONE_ERROR) ||
                model.containsAttribute(PASSWORD_ERROR) ||
                model.containsAttribute(CONFIRM_PASSWORD_ERROR)) {
            LOGGER.warn("Registration validation failed for email: {}", accountVo.getEmail());
            result.put(IS_VALID, false);
        } else {
            LOGGER.info("Registration validation successful for email: {}", accountVo.getEmail());
        }

        return result;
    }

    /**
     * Validates that all required fields are provided
     *
     * @param accountVo The account view object
     * @param model     The model to add attribute errors to
     * @return true if there are errors, false otherwise
     */
    @Override
    public boolean validateRegisterRequired(AccountVo accountVo, Model model) {
        LOGGER.debug("Validating required fields for registration");

        boolean hasError = false;

        if (requiredField(accountVo.getFullName()) && !model.containsAttribute(FULL_NAME_ERROR)) {
            LOGGER.warn("Full name is required but not provided");
            model.addAttribute(FULL_NAME_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        if (requiredField(accountVo.getEmail()) && !model.containsAttribute(EMAIL_ERROR)) {
            LOGGER.warn("Email is required but not provided");
            model.addAttribute(EMAIL_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        if (requiredField(accountVo.getPhone()) && !model.containsAttribute(PHONE_ERROR)) {
            LOGGER.warn("Phone is required but not provided");
            model.addAttribute(PHONE_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        if (requiredField(accountVo.getPassword()) && !model.containsAttribute(PASSWORD_ERROR)) {
            LOGGER.warn("Password is required but not provided");
            model.addAttribute(PASSWORD_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        if (requiredField(accountVo.getConfirmPassword()) && !model.containsAttribute(CONFIRM_PASSWORD_ERROR)) {
            LOGGER.warn("Confirm password is required but not provided");
            model.addAttribute(CONFIRM_PASSWORD_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }

        return hasError;
    }

    /**
     * Validates that all fields have valid data
     *
     * @param accountVo The account view object
     * @param model     The model to add attribute errors to
     * @return true if there are errors, false otherwise
     */
    @Override
    public boolean validateValidRegister(AccountVo accountVo, Model model) {
        LOGGER.debug("Validating field formats for registration");
        boolean hasError = false;
        if (!model.containsAttribute(EMAIL_ERROR) && !validEmail(accountVo.getEmail())) {
            LOGGER.warn("Invalid email format: {}", accountVo.getEmail());
            model.addAttribute(EMAIL_ERROR, globalConfig.getValidEmail());
            hasError = true;
        }
        if (!model.containsAttribute(PASSWORD_ERROR) && !validPassword(accountVo.getPassword())) {
            LOGGER.warn("Invalid password format");
            model.addAttribute(PASSWORD_ERROR, globalConfig.getValidatePassword());
            hasError = true;
        }
        if (!model.containsAttribute(PHONE_ERROR) && !validPhoneNumber(accountVo.getPhone())) {
            LOGGER.warn("Invalid phone format: {}", accountVo.getPhone());
            model.addAttribute(PHONE_ERROR, globalConfig.getPhoneIsNotValid());
            hasError = true;
        }
        if (!model.containsAttribute(CONFIRM_PASSWORD_ERROR) &&
                !checkPasswordsMatch(accountVo.getPassword(), accountVo.getConfirmPassword())) {
            LOGGER.warn("Passwords do not match");
            model.addAttribute(CONFIRM_PASSWORD_ERROR, globalConfig.getPasswordNotMatch());
            hasError = true;
        }
        return hasError;
    }

    /**
     * Checks for duplicate email or phone in registration
     *
     * @param accountVo The account view object
     * @param model     The model to add attribute errors to
     */
    @Override
    public void validateDuplicateRegister(AccountVo accountVo, Model model) {
        LOGGER.debug("Checking for duplicate email/phone during registration");
        if (!model.containsAttribute(EMAIL_ERROR) && checkDuplicateEmail(accountVo.getEmail())) {
            LOGGER.warn("Email already exists: {}", accountVo.getEmail());
            model.addAttribute(EMAIL_ERROR, globalConfig.getEmailExist());
        }
        if (!model.containsAttribute(PHONE_ERROR) && checkDuplicatePhone(accountVo.getPhone())) {
            LOGGER.warn("Phone already exists: {}", accountVo.getPhone());
            model.addAttribute(PHONE_ERROR, globalConfig.getPhoneIsExist());
        }
    }

    /**
     * Validates a password change request
     *
     * @param oldPassword     The current password
     * @param newPassword     The new password
     * @param confirmPassword The confirmation of the new password
     * @param model           The model to add attribute errors to
     * @return A map containing validation results
     */
    @Override
    public Map<String, Object> validateChangePassword(String oldPassword,
                                                      String newPassword,
                                                      String confirmPassword,
                                                      Model model) {
        LOGGER.debug("Starting password change validation");

        Map<String, Object> passwordChangeMap = new HashMap<>();
        passwordChangeMap.put(IS_VALID, true);
        validateChangePasswordRequired(oldPassword, newPassword, confirmPassword, model);
        validateValidChangePasswordFormats(oldPassword, newPassword, confirmPassword, model);
        validatePasswordChangeRules(oldPassword, newPassword, confirmPassword, model);
        if (model.containsAttribute(NEW_PASSWORD_ERROR) ||
                model.containsAttribute(PASSWORD_ERROR) ||
                model.containsAttribute(CONFIRM_PASSWORD_ERROR)) {
            LOGGER.warn("Password change validation failed");
            passwordChangeMap.put(IS_VALID, false);
        } else {
            LOGGER.info("Password change validation successful");
        }

        return passwordChangeMap;
    }

    @Override
    public boolean validateResetPassword(String password, String confirmPassword, Model model) {
        LOGGER.debug("Starting reset password validation");
        boolean hasError = false;
        validateResetPasswordRequired(password, confirmPassword, model);
        validateValidResetPasswordFormats(password, confirmPassword, model);
        validatePasswordResetRules(password, confirmPassword, model);
        if (model.containsAttribute(NEW_PASSWORD_ERROR) || model.containsAttribute(CONFIRM_PASSWORD_ERROR)) {
            LOGGER.warn("Valid ResetPassword validation failed");
            hasError = true;
        }
        return hasError;
    }

    @Override
    public void validatePasswordResetRules(String newPassword, String confirmPassword, Model model) {
        if (!model.containsAttribute(CONFIRM_PASSWORD_ERROR) &&
                !checkPasswordsMatch(newPassword, confirmPassword)) {
            LOGGER.warn("Password and confirm password do not match");
            model.addAttribute(CONFIRM_PASSWORD_ERROR, globalConfig.getPasswordNotMatch());
        }
    }

    @Override
    public boolean validateUpdateAccountReq(AccountInfo accountInfo, Model model) {
        LOGGER.debug("Validating required fields for update account");
        boolean hasError = false;
        if (requiredField(accountInfo.getFullName())) {
            LOGGER.warn("full name is required but not provided");
            model.addAttribute(FULL_NAME_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        if (requiredField(accountInfo.getPhone())) {
            LOGGER.warn("phone is required but not provided");
            model.addAttribute(PHONE_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        return hasError;
    }

    @Override
    public boolean validateUpdateAccountValid(AccountInfo accountInfo, Model model) {
        boolean hasError = false;
        if (!model.containsAttribute(PHONE_ERROR) && !validPhoneNumber(accountInfo.getPhone())) {
            LOGGER.warn("phone number is not valid");
            model.addAttribute(PHONE_ERROR, globalConfig.getPhoneIsNotValid());
            hasError = true;
        }
        if (accountInfo.getDob()!=null &&!accountInfo.getDob().isBefore(LocalDate.of(2006, 1, 1))) {
            LOGGER.warn("Dob  is not valid");
            model.addAttribute("dobError", globalConfig.getInvalidDate());
            hasError = true;
        }
        return hasError;
    }

    @Override
    public boolean validateUpdateAccountDuplicate(String currentPhone, String newPhone, Model model) {
        if (!model.containsAttribute(PHONE_ERROR)) {
            if (!currentPhone.equals(newPhone) && !checkDuplicatePhone(newPhone)) {
                LOGGER.warn("Phone already exists with other account: {}", newPhone);
                model.addAttribute(PHONE_ERROR, globalConfig.getPhoneIsExist());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateUpdateAccount(AccountInfo accountInfo, String currentPhone, Model model) {
        LOGGER.debug("Validating update account process");

        boolean hasError = false;

        if (validateUpdateAccountReq(accountInfo, model)) {
            hasError = true;
        }
        if (validateUpdateAccountValid(accountInfo, model)) {
            hasError = true;
        }
        if (validateUpdateAccountDuplicate(currentPhone, accountInfo.getPhone(), model)) {
            hasError = true;
        }
        return hasError;
    }

    @Override
    public boolean validateForgotPassword(String email, Model model) {
        LOGGER.debug("Starting forgot password process");
        boolean hasError = false;
        if (requiredField(email)) {
            model.addAttribute(EMAIL_ERROR, globalConfig.getRequiredField());
        } else if (!validEmail(email)) {
            model.addAttribute(EMAIL_ERROR, globalConfig.getValidEmail());
        } else if (!checkDuplicateEmail(email)) {
            model.addAttribute(EMAIL_ERROR, globalConfig.getEmailNotExist());
        }
        if (model.containsAttribute(EMAIL_ERROR)) {
            LOGGER.warn("Forgot  password validation failed");
            hasError = true;
        }
        return hasError;
    }

    @Override
    public boolean validateResetPasswordRequired(String newPassword, String confirmPassword, Model model) {
        LOGGER.debug("Starting validate password required fields");
        boolean hasError = false;
        if (requiredField(newPassword)) {
            LOGGER.warn("New password required");
            model.addAttribute(NEW_PASSWORD_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        if (requiredField(confirmPassword)) {
            LOGGER.warn("Confirm password required");
            model.addAttribute(CONFIRM_PASSWORD_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        return hasError;
    }

    @Override
    public boolean validateValidResetPasswordFormats(String newPassword, String confirmPassword, Model model) {
        LOGGER.debug("Starting validate password formats");
        boolean hasError = false;
        if (!model.containsAttribute(NEW_PASSWORD_ERROR) && !validPassword(newPassword)) {
            model.addAttribute(NEW_PASSWORD_ERROR, globalConfig.getValidatePassword());
            hasError = true;
        }
        if (!model.containsAttribute(CONFIRM_PASSWORD_ERROR) && !validPassword(confirmPassword)) {
            model.addAttribute(CONFIRM_PASSWORD_ERROR, globalConfig.getValidatePassword());
            hasError = true;
        }
        return hasError;
    }


    /**
     * Validates that all required fields for password change are provided
     *
     * @param oldPassword     The current password
     * @param newPassword     The new password
     * @param confirmPassword The confirmation of the new password
     * @param model           The model to add attribute errors to
     * @return true if there are errors, false otherwise
     */
    @Override
    public boolean validateChangePasswordRequired(String oldPassword,
                                                  String newPassword,
                                                  String confirmPassword, Model model) {
        LOGGER.debug("Validating required fields for password change");
        boolean hasError = false;
        if (requiredField(oldPassword)) {
            LOGGER.warn("Old password is required but not provided");
            model.addAttribute(PASSWORD_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        if (requiredField(newPassword)) {
            LOGGER.warn("New password is required but not provided");
            model.addAttribute(NEW_PASSWORD_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }
        if (requiredField(confirmPassword)) {
            LOGGER.warn("Confirm password is required but not provided");
            model.addAttribute(CONFIRM_PASSWORD_ERROR, globalConfig.getRequiredField());
            hasError = true;
        }

        return hasError;
    }

    /**
     * Validates that all password fields have valid formats
     *
     * @param oldPassword     The current password
     * @param newPassword     The new password
     * @param confirmPassword The confirmation of the new password
     * @param model           The model to add attribute errors to
     * @return true if there are errors, false otherwise
     */
    @Override
    public boolean validateValidChangePasswordFormats(String oldPassword,
                                                      String newPassword,
                                                      String confirmPassword, Model model) {
        LOGGER.debug("Validating password formats for password change");

        boolean hasError = false;

        if (!model.containsAttribute(PASSWORD_ERROR) && !validPassword(oldPassword)) {
            LOGGER.warn("Old password has invalid format");
            model.addAttribute(PASSWORD_ERROR, globalConfig.getValidatePassword());
            hasError = true;
        }
        if (!model.containsAttribute(NEW_PASSWORD_ERROR) && !validPassword(newPassword)) {
            LOGGER.warn("New password has invalid format");
            model.addAttribute(NEW_PASSWORD_ERROR, globalConfig.getValidatePassword());
            hasError = true;
        }
        if (!model.containsAttribute(CONFIRM_PASSWORD_ERROR) && !validPassword(confirmPassword)) {
            LOGGER.warn("Confirm password has invalid format");
            model.addAttribute(CONFIRM_PASSWORD_ERROR, globalConfig.getValidatePassword());
            hasError = true;
        }

        return hasError;
    }

    /**
     * Validates business rules for password change
     *
     * @param oldPassword     The current password
     * @param newPassword     The new password
     * @param confirmPassword The confirmation of the new password
     * @param model           The model to add attribute errors to
     */
    @Override
    public void validatePasswordChangeRules(String oldPassword,
                                            String newPassword,
                                            String confirmPassword,
                                            Model model) {
        LOGGER.debug("Validating password change rules");
        // Check if new password matches confirmation
        if (!model.containsAttribute(PASSWORD_ERROR) && !checkOldPassword(oldPassword)) {
            LOGGER.warn("Current password is wrong.");
            model.addAttribute(PASSWORD_ERROR, globalConfig.getOldPasswordWrong());
        }
        if (!model.containsAttribute(CONFIRM_PASSWORD_ERROR) &&
                !checkPasswordsMatch(newPassword, confirmPassword)) {
            LOGGER.warn("New password and confirm password do not match");
            model.addAttribute(CONFIRM_PASSWORD_ERROR, globalConfig.getPasswordNotMatch());
        }
        // Check if old and new passwords are the same
        if (!model.containsAttribute(NEW_PASSWORD_ERROR) &&
                checkPasswordsMatch(oldPassword, newPassword)) {
            LOGGER.warn("New password is the same as old password");
            model.addAttribute(NEW_PASSWORD_ERROR, globalConfig.getNewPasswordWrong());
        }
    }

    /**
     * Checks if two passwords match
     *
     * @param password        The password
     * @param confirmPassword The confirmation password
     * @return true if the passwords match, false otherwise
     */
    @Override
    public boolean checkPasswordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    /**
     * Checks if the provided password matches the current user's password
     *
     * @param password The password to check
     * @return true if the password matches, false otherwise
     */
    @Override
    public boolean checkOldPassword(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        LOGGER.debug("Checking old password for user: {}", email);
        AccountInfo accountInfo = accountRepository.findByEmail(email);
        if (accountInfo == null) {
            LOGGER.warn("Account not found for email: {}", email);
            return false;
        }
        // Use password encoder to check if the provided password matches the stored one
        String storedPassword = accountInfo.getPassword().replace("{bcrypt}", "");
        boolean matches = passwordEncoder.matches(password, storedPassword);
        if (!matches) {
            LOGGER.warn("Old password verification failed for user: {}", email);
        } else {
            LOGGER.debug("Old password verification successful for user: {}", email);
        }
        return matches;
    }

}
