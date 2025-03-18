package fa.appcode.services.impl;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.exceptions.ExceptionCustom;
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
    static final String IS_VALID = "isValid";
    static final String VALIDATION_LEVEL = "validationLevel";

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


        @Override
        public Map<String, Object> validateRegistration(AccountVo accountVo) {
            LOGGER.debug("Validating registration for email: {}", accountVo.getEmail());
            Map<String, Object> result = new HashMap<>();
            result.put(IS_VALID, true);
            Map<String, String> errors = new HashMap<>();
            Map<String, String> requiredErrors = validateRegisterRequired(accountVo);
            errors.putAll(requiredErrors);
            validateFullName(accountVo.getFullName(), requiredErrors.containsKey(FULL_NAME_ERROR), errors);
            validateEmail(accountVo.getEmail(), requiredErrors.containsKey(EMAIL_ERROR), errors);
            validatePhone(accountVo.getPhone(), requiredErrors.containsKey(PHONE_ERROR), errors);
            validatePasswords(accountVo.getPassword(), accountVo.getConfirmPassword(),
                    requiredErrors.containsKey(PASSWORD_ERROR) || requiredErrors.containsKey(CONFIRM_PASSWORD_ERROR),
                    errors);
            if (!errors.isEmpty()) {
                setValidationResult(result, errors, requiredErrors.isEmpty());
            } else {
                LOGGER.info("Registration validation successful for email: {}", accountVo.getEmail());
            }
            return result;
        }

    /**
     * Validates email format and checks for duplicates
     */
    private void validateEmail(String email, boolean hasRequiredError, Map<String, String> errors) {
        if (hasRequiredError) {
            return;
        }
        if (!validEmail(email)) {
            LOGGER.warn("Invalid email format: {}", email);
            errors.put(EMAIL_ERROR, globalConfig.getValidEmail());
        } else if (checkDuplicateEmail(email)) {
            LOGGER.warn("Email already exists: {}", email);
            errors.put(EMAIL_ERROR, globalConfig.getEmailExist());
        }
    }

    private void validateFullName(String fullName, boolean hasRequiredError, Map<String, String> errors) {
        if (hasRequiredError) {
            return;
        }
        if (fullName.length() > 100) {
            LOGGER.warn("Invalid full name format: {}", fullName);
            errors.put(FULL_NAME_ERROR, globalConfig.getInvalidFullName());
        }
    }

    /**
     * Validates phone format and checks for duplicates
     */
    private void validatePhone(String phone, boolean hasRequiredError, Map<String, String> errors) {
        if (hasRequiredError) {
            return;
        }
        if (!validPhoneNumber(phone)) {
            LOGGER.warn("Invalid phone format: {}", phone);
            errors.put(PHONE_ERROR, globalConfig.getPhoneIsNotValid());
        } else if (checkDuplicatePhone(phone)) {
            LOGGER.warn("Phone already exists: {}", phone);
            errors.put(PHONE_ERROR, globalConfig.getPhoneIsExist());
        }
    }

    /**
     * Validates password format and confirms passwords match
     */
    private void validatePasswords(String password, String confirmPassword, boolean hasRequiredError, Map<String, String> errors) {
        if (hasRequiredError) {
            return;
        }
        if (!validPassword(password)) {
            LOGGER.warn("Invalid password format");
            errors.put(PASSWORD_ERROR, globalConfig.getValidatePassword());
        }

        if (!checkPasswordsMatch(password, confirmPassword)) {
            LOGGER.warn("Passwords do not match");
            errors.put(CONFIRM_PASSWORD_ERROR, globalConfig.getPasswordNotMatch());
        }
    }

    /**
     * Sets the validation result with appropriate validation level
     */
    private void setValidationResult(Map<String, Object> result, Map<String, String> errors, boolean passedRequiredCheck) {
        result.put(IS_VALID, false);
        result.put("errors", errors);
        if (!passedRequiredCheck) {
            result.put(VALIDATION_LEVEL, "required");
        } else if (hasFormatErrors(errors)) {
            result.put(VALIDATION_LEVEL, "format");
        } else {
            result.put(VALIDATION_LEVEL, "duplicate");
        }
    }

    /**
     * Determines if there are format validation errors
     */
    private boolean hasFormatErrors(Map<String, String> errors) {
        return errors.containsKey(PASSWORD_ERROR) ||
                errors.containsKey(CONFIRM_PASSWORD_ERROR) ||
                errors.get(EMAIL_ERROR) != null && errors.get(EMAIL_ERROR).equals(globalConfig.getValidEmail()) ||
                errors.get(PHONE_ERROR) != null && errors.get(PHONE_ERROR).equals(globalConfig.getPhoneIsNotValid());
    }

    @Override
    public Map<String, String> validateRegisterRequired(AccountVo accountVo) {
        LOGGER.debug("Validating required fields for registration");
        Map<String, String> errors = new HashMap<>();

        if (requiredField(accountVo.getFullName())) {
            LOGGER.warn("Full name is required but not provided");
            errors.put(FULL_NAME_ERROR, globalConfig.getRequiredField());
        }
        if (requiredField(accountVo.getEmail())) {
            LOGGER.warn("Email is required but not provided");
            errors.put(EMAIL_ERROR, globalConfig.getRequiredField());
        }
        if (requiredField(accountVo.getPhone())) {
            LOGGER.warn("Phone is required but not provided");
            errors.put(PHONE_ERROR, globalConfig.getRequiredField());
        }
        if (requiredField(accountVo.getPassword())) {
            LOGGER.warn("Password is required but not provided");
            errors.put(PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        if (requiredField(accountVo.getConfirmPassword())) {
            LOGGER.warn("Confirm password is required but not provided");
            errors.put(CONFIRM_PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        return errors;
    }

    /**
     * Validates the email input for the forgot password process.
     *
     * @param email The email address to validate.
     * @return A map containing error messages if validation fails.
     */
    public Map<String, String> validateForgotPassword(String email) {
        // Log the start of the validation process
        LOGGER.debug("Starting forgot password validation");

        // Create a map to store validation errors
        Map<String, String> errors = new HashMap<>();

        // Check if the email field is empty or null
        if (requiredField(email)) {
            LOGGER.warn("Email is required but not provided");
            errors.put(EMAIL_ERROR, globalConfig.getRequiredField());
        }
        // Check if the email format is valid
        else if (!validEmail(email)) {
            LOGGER.warn("Email is not valid");
            errors.put(EMAIL_ERROR, globalConfig.getValidEmail());
        }

        // Return the map containing any validation errors
        return errors;
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
    public Map<String, String> validateResetPassword(String newPassword, String confirmPassword) {
        LOGGER.debug("Starting reset password validation");
        Map<String, String> errors = new HashMap<>();
        if (requiredField(newPassword)) {
            LOGGER.warn("New password is required but not provided");
            errors.put(NEW_PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        if (requiredField(confirmPassword)) {
            LOGGER.warn("Confirm password is required but not provided");
            errors.put(CONFIRM_PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        if (!errors.containsKey(NEW_PASSWORD_ERROR) && !validPassword(newPassword)) {
            LOGGER.warn("New password is not valid");
            errors.put(NEW_PASSWORD_ERROR, globalConfig.getValidatePassword());
        }
        if (!errors.containsKey(NEW_PASSWORD_ERROR) && !errors.containsKey(CONFIRM_PASSWORD_ERROR)
                && !confirmPassword.equals(newPassword)) {
            LOGGER.warn("Confirm password is not equal");
            errors.put(CONFIRM_PASSWORD_ERROR, globalConfig.getPasswordNotMatch());
        }
        return errors;
    }

    @Override
    public void validateReset(String newPassword, String confirmPassword) {
        LOGGER.debug("Starting forgot password validation");
        Map<String, String> errors = new HashMap<>();

        if (requiredField(newPassword)) {
            LOGGER.warn("New password is required but not provided");
            errors.put(NEW_PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        if (requiredField(confirmPassword)) {
            LOGGER.warn("Confirm password is required but not provided");
            errors.put(CONFIRM_PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        if (!errors.containsKey(NEW_PASSWORD_ERROR) && !validPassword(newPassword)) {
            LOGGER.warn("New password is not valid");
            errors.put(NEW_PASSWORD_ERROR, globalConfig.getValidatePassword());
        }
        if (!errors.containsKey(NEW_PASSWORD_ERROR) && !errors.containsKey(CONFIRM_PASSWORD_ERROR)
                && !confirmPassword.equals(newPassword)) {
            LOGGER.warn("Confirm password is not equal");
            errors.put(CONFIRM_PASSWORD_ERROR, globalConfig.getPasswordNotMatch());
        }
        if (!errors.isEmpty()) {
            throw new ExceptionCustom(errors);
        }
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
    public Map<String, String> validateAccountField(String fullName, String currentPhone, String newPhone, LocalDate dob) {
        Map<String, String> validationErrors = new HashMap<>();
        validationErrors.putAll(fullNameValidation(fullName));
        validationErrors.putAll(validatePhone(currentPhone, newPhone));
        if (dob != null && !dob.isBefore(LocalDate.of(2006, 1, 1))) {
            validationErrors.put("dobError", globalConfig.getInvalidDate());
        }
        return validationErrors;
    }

    public Map<String, String> fullNameValidation(String fullName) {
        Map<String, String> fullNameErrors = new HashMap<>();
        if (requiredField(fullName)) {
            fullNameErrors.put(FULL_NAME_ERROR, globalConfig.getRequiredField());
            return fullNameErrors;
        } else if (fullName.length() > 250) {
            fullNameErrors.put(FULL_NAME_ERROR, globalConfig.getInvalidFullName());
            return fullNameErrors;
        }
        return fullNameErrors;
    }

    public Map<String, String> validatePhone(String phone, String newPhone) {
        Map<String, String> phoneErrors = new HashMap<>();
        if (requiredField(phone)) {
            phoneErrors.put(PHONE_ERROR, globalConfig.getRequiredField());
            return phoneErrors;
        } else if (!validPhoneNumber(phone)) {
            phoneErrors.put(PHONE_ERROR, globalConfig.getPhoneIsNotValid());
            return phoneErrors;
        } else if (!newPhone.equals(phone) && accountRepository.existsByPhone(phone)) {
            phoneErrors.put(PHONE_ERROR, globalConfig.getPhoneIsExist());
            return phoneErrors;
        }
        return phoneErrors;
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
        if (accountInfo.getDob() != null && !accountInfo.getDob().isBefore(LocalDate.of(2006, 1, 1))) {
            LOGGER.warn("Dob  is not valid");
            model.addAttribute("dobError", globalConfig.getInvalidDate());
            hasError = true;
        }
        return hasError;
    }

    @Override
    public boolean validateUpdateAccountDuplicate(String currentPhone, String newPhone, Model model) {
        if (!model.containsAttribute(PHONE_ERROR)) {
            if (!currentPhone.equals(newPhone) && checkDuplicatePhone(newPhone)) {
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
     * @return true if there are errors, false otherwise
     */
    @Override
    public Map<String, String> validateChangePasswordRequired(String oldPassword,
                                                              String newPassword,
                                                              String confirmPassword) {
        LOGGER.debug("Validating required fields for password change");
        Map<String, String> changePasswordError = new HashMap<>();
        if (requiredField(oldPassword)) {
            LOGGER.warn("Old password is required but not provided");
            changePasswordError.put(PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        if (requiredField(newPassword)) {
            LOGGER.warn("New password is required but not provided");
            changePasswordError.put(NEW_PASSWORD_ERROR, globalConfig.getRequiredField());

        }
        if (requiredField(confirmPassword)) {
            LOGGER.warn("Confirm password is required but not provided");
            changePasswordError.put(CONFIRM_PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        return changePasswordError;
    }

    /**
     * Validates that all password fields have valid formats
     *
     * @param oldPassword     The current password
     * @param newPassword     The new password
     * @param confirmPassword The confirmation of the new password
     * @return true if there are errors, false otherwise
     */
    @Override
    public Map<String, String> validateValidChangePasswordFormats(String oldPassword,
                                                                  String newPassword,
                                                                  String confirmPassword) {
        LOGGER.debug("Validating password formats for password change");
        Map<String, String> changePasswordError = validateChangePasswordRequired(oldPassword, newPassword, confirmPassword);
        if (!changePasswordError.containsKey(PASSWORD_ERROR) && !validPassword(oldPassword)) {
            LOGGER.warn("Old password has invalid format");
            changePasswordError.put(PASSWORD_ERROR, globalConfig.getValidatePassword());
        }
        if (!changePasswordError.containsKey(NEW_PASSWORD_ERROR) && !validPassword(newPassword)) {
            LOGGER.warn("New password has invalid format");
            changePasswordError.put(NEW_PASSWORD_ERROR, globalConfig.getValidatePassword());
        }
        if (!changePasswordError.containsKey(CONFIRM_PASSWORD_ERROR) && !validPassword(confirmPassword)) {
            LOGGER.warn("Confirm password has invalid format");
            changePasswordError.put(CONFIRM_PASSWORD_ERROR, globalConfig.getValidatePassword());
        }
        return changePasswordError;
    }

    /**
     * Validates business rules for password change
     *
     * @param oldPassword     The current password
     * @param newPassword     The new password
     * @param confirmPassword The confirmation of the new password
     */
    @Override
    public Map<String, String> validatePasswordChangeRules(String oldPassword,
                                                           String newPassword,
                                                           String confirmPassword) {
        LOGGER.debug("Validating password change rules");
        Map<String, String> changePasswordError = validateValidChangePasswordFormats(oldPassword, newPassword, confirmPassword);
        if (!changePasswordError.containsKey(PASSWORD_ERROR) && !checkOldPassword(oldPassword)) {
            LOGGER.warn("Current password is wrong.");
            changePasswordError.put(PASSWORD_ERROR, globalConfig.getOldPasswordWrong());
        }
        if (!changePasswordError.containsKey(CONFIRM_PASSWORD_ERROR) &&
                !checkPasswordsMatch(newPassword, confirmPassword)) {
            LOGGER.warn("New password and confirm password do not match");
            changePasswordError.put(CONFIRM_PASSWORD_ERROR, globalConfig.getPasswordNotMatch());
        }
        if (!changePasswordError.containsKey(NEW_PASSWORD_ERROR) &&
                checkPasswordsMatch(oldPassword, newPassword)) {
            LOGGER.warn("New password is the same as old password");
            changePasswordError.put(NEW_PASSWORD_ERROR, globalConfig.getNewPasswordWrong());
        }
        return changePasswordError;
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
//        String storedPassword = accountInfo.getPassword().replace("{bcrypt}", "");
        boolean matches = passwordEncoder.matches(password, accountInfo.getPassword());
        if (!matches) {
            LOGGER.warn("Old password verification failed for user: {}", email);
        } else {
            LOGGER.debug("Old password verification successful for user: {}", email);
        }
        return matches;
    }

}
