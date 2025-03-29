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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Override
    public Map<String, String> dobValidation(LocalDate dob) {
        if (dob == null) {
            return Map.of("dobError", globalConfig.getRequiredField());
        }
        if (!dob.isBefore(LocalDate.of(2006, 1, 1))) {
            return Map.of("dobError", globalConfig.getInvalidDate());
        }
        return Collections.emptyMap();
    }


    public Map<String, String> fullNameValidation(String fullName) {
        if (requiredField(fullName)) {
            return Map.of(FULL_NAME_ERROR, globalConfig.getRequiredField());
        }
        if (fullName.length() > 255) {
            return Map.of(FULL_NAME_ERROR, globalConfig.getInvalidFullName());
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> emailValidation(String email) {
        if (requiredField(email)) {
            LOGGER.debug("Email not input: {}", email);
            return Map.of(EMAIL_ERROR, globalConfig.getRequiredField());
        }
        if (!validEmail(email)) {
            LOGGER.debug("Email not valid: {}", email);
            return Map.of(EMAIL_ERROR, globalConfig.getInValidEmail());
        }
        if (accountRepository.existsByEmail(email)) {
            return Map.of(EMAIL_ERROR, globalConfig.getEmailExist());
        }
        return Collections.emptyMap();
    }

    public Map<String, String> phoneValidation(String phone) {
        if (requiredField(phone)) {
            return Map.of(PHONE_ERROR, globalConfig.getRequiredField());
        }
        if (!validPhoneNumber(phone)) {
            return Map.of(PHONE_ERROR, globalConfig.getPhoneIsNotValid());
        }
        if (accountRepository.existsByPhone(phone)) {
            return Map.of(PHONE_ERROR, globalConfig.getPhoneIsExist());
        }
        return Collections.emptyMap();
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


    /**
     * Validates the email input for the forgot password process.
     *
     * @param email The email address to validate.
     * @return A map containing error messages if validation fails.
     */
    @Override
    public Map<String, String> validateForgotPassword(String email) {
        // Log the start of the validation process
        LOGGER.debug("Starting forgot password validation");
        // Check if the email field is empty or null
        if (requiredField(email)) {
            LOGGER.warn("Email is required but not provided");
            return Map.of(EMAIL_ERROR, globalConfig.getRequiredField());
        }
        // Check if the email format is valid
        else if (!validEmail(email)) {
            LOGGER.warn("Email is not valid");
            return Map.of(EMAIL_ERROR, globalConfig.getInValidEmail());
        }
        return Collections.emptyMap();
    }


    @Override
    public Map<String, String> validateResetPassword(String newPassword, String confirmPassword) {
        LOGGER.debug("Starting reset password validation");

        if (requiredField(newPassword)) {
            LOGGER.warn("New password is required but not provided");
            return Map.of(NEW_PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        if (requiredField(confirmPassword)) {
            LOGGER.warn("Confirm password is required but not provided");
            return Map.of(CONFIRM_PASSWORD_ERROR, globalConfig.getRequiredField());
        }
        if (!validPassword(newPassword)) {
            LOGGER.warn("New password is not valid");
            return Map.of(NEW_PASSWORD_ERROR, globalConfig.getValidatePassword());
        }
        if (!validPassword(confirmPassword)) {
            LOGGER.warn("New password is not valid");
            return Map.of(CONFIRM_PASSWORD_ERROR, globalConfig.getValidatePassword());
        }
        if (!newPassword.equals(confirmPassword)) {
            LOGGER.warn("Confirm password does not match new password");
            return Map.of(CONFIRM_PASSWORD_ERROR, globalConfig.getPasswordNotMatch());
        }

        return Collections.emptyMap();
    }

    /**
     * Validates account fields including full name, phone numbers, and date of birth.
     * Combines multiple validation methods into a single map.
     *
     * @param fullName     The full name of the user.
     * @param currentPhone The current phone number of the user.
     * @param newPhone     The new phone number to be updated.
     * @param dob          The date of birth of the user.
     * @return A map containing field names as keys and validation error messages as values.
     */
    @Override
    public Map<String, String> validateAccountField(String fullName, String currentPhone, String newPhone, LocalDate dob) {
        return Stream.of(
                        fullNameValidation(fullName),  // Validate full name
                        validatePhone(currentPhone, newPhone),  // Validate phone numbers
                        dobValidation(dob)  // Validate date of birth
                )
                .flatMap(map -> map.entrySet().stream())  // Flatten maps into a stream of entries
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1));  // Merge into a single map
    }

    /**
     * Validates registration fields including full name, email, phone number, and password confirmation.
     * Combines multiple validation methods into a single map.
     *
     * @param fullName        The full name of the user.
     * @param email           The email address of the user.
     * @param phone           The phone number of the user.
     * @param password        The password entered by the user.
     * @param confirmPassword The password confirmation entered by the user.
     * @return A map containing field names as keys and validation error messages as values.
     */
    @Override
    public Map<String, String> registerValidation(String fullName, String email, String phone, String password, String confirmPassword) {
        return Stream.of(
                        fullNameValidation(fullName),  // Validate full name
                        emailValidation(email),  // Validate email
                        phoneValidation(phone),  // Validate phone number
                        validateResetPassword(password, confirmPassword)  // Validate password confirmation
                )
                .flatMap(map -> map.entrySet().stream())  // Flatten maps into a stream of entries
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));  // Merge into a single map
    }

    /**
     * Validates the information of new user for add
     *
     * @param accountVo
     * @return A map containing error messages if validation fails.
     */
    @Override
    public Map<String, String> validateAccountVo(AccountVo accountVo) {
        Map<String, String> errors = new HashMap<>();

        errors.putAll(fullNameValidation(accountVo.getFullName()));

        errors.putAll(emailValidation(accountVo.getEmail()));

        errors.putAll(phoneValidation(accountVo.getPhone()));

        errors.putAll(dobValidation(
                (accountVo.getDob() == null || accountVo.getDob().trim().isEmpty())
                        ? null
                        : LocalDate.parse(accountVo.getDob())
        ));

        if (accountVo.getRole() == null || accountVo.getRole().trim().isEmpty()) {
            errors.put("roleError", globalConfig.getRequiredField());
        }

        if (accountVo.getStatus() == null || accountVo.getStatus().trim().isEmpty()) {
            errors.put("statusError", globalConfig.getRequiredField());
        }
        return errors;
    }

    @Override
    public boolean validateSearchString(String searchString) {
        return searchString.length() <= 1000;
    }


    /**
     * Validates that all required fields for password change are provided
     *
     * @param oldPassword     The current password
     * @param newPassword     The new password
     * @param confirmPassword The confirmation of the new password
     * @return a map of errors
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
            LOGGER.warn("New password is required  not provided");
            changePasswordError.put(NEW_PASSWORD_ERROR, globalConfig.getRequiredField());

        }
        if (requiredField(confirmPassword)) {
            LOGGER.warn("Confirm password is required  not provided");
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
     * @return a map contain errors
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
     * @return a map contain errors
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
        boolean matches = passwordEncoder.matches(password, accountInfo.getPassword());
        if (!matches) {
            LOGGER.warn("Old password verification failed for user: {}", email);
        } else {
            LOGGER.debug("Old password verification successful for user: {}", email);
        }
        return matches;
    }


}
