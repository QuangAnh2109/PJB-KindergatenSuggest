package fa.appcode.services.impl;

import com.cloudinary.utils.StringUtils;
import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.*;

import fa.appcode.config.GlobalConfig;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.exceptions.EntityNotFoundException;
import fa.appcode.exceptions.ValidateParentException;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.*;
import jakarta.transaction.Transactional;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import fa.appcode.services.MasterDatumService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    @Autowired
    private MasterDatumService masterDatumService;

    @Autowired
    private AccountRepository accountRepository;
    private final ValidateService validateService;
    private static final Logger LOGGER = Log4jUtils.getLogger(AccountServiceImpl.class);
    private final GlobalConfig globalConfig;
    private final CityService cityService;
    private final EmailService emailService;
    private Map<String, Object> lastValidationResult;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String ERROR_ATTRIBUTE = "error";

    public AccountInfo getAccountById(int id) {
        return accountRepository.getAccountInfoById(id,Constant.STATUS_ACTIVE);
    }

    @Override
    public AccountInfo findAccountInfoByPhone(String phone) {
        return accountRepository.findAccountByPhone(phone);
    }

//    @Override
//    public String encodePassword(String password) {
//        return "{bcrypt}" + passwordEncoder.encode(password);
//    }
    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    @Override
    public void save(AccountInfo accountInfo) {
        accountRepository.save(accountInfo);
    }

    @Override
    public AccountInfo findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


    @Override
    public boolean updatePassword(String email, String newPassword) {
        AccountInfo account = accountRepository.findByEmail(email);
        String encodedPassword = encodePassword(newPassword);
        account.setPassword(encodedPassword);
        account.setUpdateTime(Instant.now());
        account.setDatetimeChangePass(Instant.now());
        account.setRecordNo(account.getRecordNo() + 1);
        accountRepository.save(account);
        return true;
    }

    void updatePassword(AccountInfo account, String newPassword) {
        String encodedPassword = encodePassword(newPassword);
        account.setPassword(encodedPassword);
        account.setUpdateTime(Instant.now());
        account.setDatetimeChangePass(Instant.now());
        account.setRecordNo(account.getRecordNo() + 1);
        accountRepository.save(account);
    }

    @Override
    public AccountInfo createAccount(AccountVo accountVo) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFullName(accountVo.getFullName());
        accountInfo.setEmail(accountVo.getEmail());
        accountInfo.setPassword(encodePassword(accountVo.getPassword()));
        accountInfo.setPhone(accountVo.getPhone());
        accountInfo.setStatusId(Constant.STATUS_INACTIVE);
        accountInfo.setRoleId(Constant.PARENT_ROLE_ID);
        accountInfo.setImageUrl(null);
        accountInfo.setRecordNo(1);
        accountInfo.setCreateId(Constant.WEB_SYSTEM);
        accountInfo.setUpdateId(Constant.WEB_SYSTEM);
        Instant now = Instant.now();
        accountInfo.setCreateTime(now);
        accountInfo.setUpdateTime(now);
        return accountRepository.save(accountInfo);
    }

    // Get list of user account
    @Override
    public Page<AccountVo> getAllAccounts(String search, Pageable pageable) throws Exception {
        return accountRepository.findAllWithFullAddress(search, pageable);
    }

    // Find account by Id
    @Override
    public AccountVo getAccountById(Integer id) {
        AccountInfo accountInfo = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        AccountVo accountVo = new AccountVo();
        accountVo.setId(accountInfo.getId());
        accountVo.setFullName(accountInfo.getFullName());
        accountVo.setEmail(accountInfo.getEmail());
        accountVo.setPhone(accountInfo.getPhone());
        accountVo.setDob(accountInfo.getDob() != null ? accountInfo.getDob().toString() : null);
        accountVo.setImageUrl(accountInfo.getImageUrl());

        // Build full address
        if (accountInfo.getAddress() == null && accountInfo.getWard() == null &&
                accountInfo.getDistrict() == null && accountInfo.getCity() == null) {
            accountVo.setFullAddress("No specific information yet");
        } else {
            StringBuilder fullAddress = new StringBuilder(accountInfo.getAddress() != null ? accountInfo.getAddress() : "");
            if (accountInfo.getWard() != null) {
                fullAddress.append(", ").append(accountInfo.getWard().getWardName());
            }
            if (accountInfo.getDistrict() != null) {
                fullAddress.append(", ").append(accountInfo.getDistrict().getDistrictName());
            }
            if (accountInfo.getCity() != null) {
                fullAddress.append(", ").append(accountInfo.getCity().getCityName());
            }
            accountVo.setFullAddress(fullAddress.toString().trim());
        }
        // Resolve role and status names
        accountVo.setRole(masterDatumService.getMasterByTypeNameAndTypeKey("ROLE", accountInfo.getRoleId()));
        accountVo.setStatus(masterDatumService.getMasterByTypeNameAndTypeKey("ACCOUNT STATUS", accountInfo.getStatusId()));
        accountVo.setRecordNo(accountInfo.getRecordNo());
        return accountVo;
    }


    // Update user account
    @Override
    public int updateAccount(AccountVo accountVo) {
        AccountInfo user = accountRepository.findById(accountVo.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Log4jUtils.getLogger().info("recordNo get in DB : {}", user.getRecordNo());

        if (!user.getRecordNo().equals(accountVo.getRecordNo())) {
            throw new IllegalStateException("Data has been modified by someone else!"); // Xử lý lỗi ở Service
        }
        // Update role or status of account
        user.setRoleId(masterDatumService.getMasterKeyByTypeNameAndTypeValue("ROLE", accountVo.getRole()));
        user.setStatusId(masterDatumService.getMasterKeyByTypeNameAndTypeValue("ACCOUNT STATUS", accountVo.getStatus()));

        int newRecordNo = user.getRecordNo() + 1;
        user.setRecordNo(newRecordNo);
        user.setUpdateId("SYSTEM_ADMIN");
        user.setUpdateTime(Instant.now());

        accountRepository.save(user);

        return newRecordNo;
    }


    // Delete logic user account
    @Override
    public void deleteAccount(Integer id) {
        AccountInfo account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        account.setDeleteFlg(true);
        accountRepository.save(account);
    }

    @Override
    public void addUserFromAdmin(AccountVo accountVo, Principal principal) {

        // Generate password by system
        String randomPassword = UUID.randomUUID().toString();
        accountVo.setPassword(randomPassword);
        accountVo.setConfirmPassword(randomPassword);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFullName(accountVo.getFullName());
        accountInfo.setEmail(accountVo.getEmail());
        accountInfo.setPhone(accountVo.getPhone());
        accountInfo.setDob(LocalDate.parse(accountVo.getDob()));
        accountInfo.setRoleId(masterDatumService.getMasterKeyByTypeNameAndTypeValue("ROLE", accountVo.getRole()));
        accountInfo.setPassword(encodePassword(accountVo.getPassword()));
        accountInfo.setStatusId(masterDatumService.getMasterKeyByTypeNameAndTypeValue("ACCOUNT STATUS", accountVo.getStatus())); // Default status
        accountInfo.setImageUrl("null");
        accountInfo.setRecordNo(1);
        accountInfo.setCreateId("SYSTEM_ADMIN");
        accountInfo.setUpdateId("SYSTEM_ADMIN");
        accountInfo.setCreateTime(Instant.now());
        accountInfo.setUpdateTime(Instant.now());
        accountRepository.save(accountInfo);

        // Send mail
        emailService.sendEmailToMany(SendMailInfo.builder()
                .toMail(List.of(accountVo.getEmail()))
                .ccMail(List.of())
                .mailId(2)
                .detail(Map.of(Placeholder.USER_NAME, accountVo.getEmail(),
                        Placeholder.EMAIL, accountVo.getEmail(),
                        Placeholder.PASSWORD, randomPassword,
                        Placeholder.OWNER_ACCOUNT, this.getAccountInfo(principal).getFullName()))
                .build());
    }


    // ========================================================


    @Override
    public Page<ParentVo> findAllParent(Pageable pageable) {
        return null;
    }

    //=========================================================

    public Page<ParentVo> findAllParent(String search, Pageable pageable) {
        return accountRepository.findAllParent(search, pageable,Constant.STATUS_ACTIVE);
    }

    @Override
    public ParentVo findParentById(int id) throws ValidateParentException {
        ParentVo parent = accountRepository.findParentById(id,Constant.STATUS_ACTIVE);
        if (parent == null) {
            throw new ValidateParentException("This Parent is current Inactive, Deleted or not Exist");
        }
        return parent;
    }

    @Override
    public Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id, Pageable pageable) {
        return null;
    }


    @Override
    public String findAccountRoleString(String email) {
        return accountRepository.findAccountRoleString(email);
    }

    @Override
    public AccountInfo getAccountInfoById(int id) {
        return accountRepository.getAccountInfoById(id,Constant.STATUS_ACTIVE);
    }


    @Override
    public Page<ParentVo> findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(String email, String search, Pageable pageable) {
        return accountRepository.findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(email, search, pageable,Constant.STATUS_ACTIVE);
    }

    @Override
    public AccountInfo getAccountInfo(Principal principal) {
        String user = principal.getName();
        AccountInfo account = findByEmail(user);
        return account;
    }

    @Override
    public void updateAccountInfo(AccountInfo existing, AccountInfo formData) {
        existing.setFullName(formData.getFullName());
        existing.setPhone(formData.getPhone());
        existing.setDob(formData.getDob());
        existing.setUpdateTime(Instant.now());
        existing.setCity(formData.getCity());
        existing.setDistrict(formData.getDistrict());
        existing.setWard(formData.getWard());
        existing.setAddress(formData.getAddress());
        existing.setRecordNo(existing.getRecordNo() + 1);
        accountRepository.save(existing);
    }

    @Override
    public void saveAccountInfo(AccountInfo accountInfo) {
        accountRepository.save(accountInfo);
    }


    @Override
    public int getAccountIdByEmail(String email) {
        return accountRepository.findAccountByEmailAndStatusIdAndDeleteFlg(email, 1, false).getId();
    }

    //    @Override
//    public AccountInfo findWithFullAddressByEmail(String email, boolean deleteFlg) {
//        return accountRepository.findWithFullAddressByEmail(email, deleteFlg);
//    }
    @Override
    public String getEmailByAccountIdAndActiveAndNoDelete(int accountId) {
        return accountRepository.getEmailByAccountIdAndStatusIdAndDeleteFlg(accountId, 1, false);
    }

    @Override
    public String getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(int id) {
        return accountRepository.getSchoolOwnerEmailBySchoolIdAndStatusAndDeleteFlg(id, 1, false);
    }


    @Override
    public Map<String, Object> getValidationResult() {
        return lastValidationResult;
    }

    @Override
    public boolean processRegister(AccountVo accountVo) {
        try {
            lastValidationResult = validateService.validateRegistration(accountVo);
            if (!(boolean) lastValidationResult.get("isValid")) {
                String validationLevel = (String) lastValidationResult.get("validationLevel");
                LOGGER.warn("Validation failed at level: {} - email: {}", validationLevel, accountVo.getEmail());
                return false;
            }
            createAccount(accountVo);
            LOGGER.info("Account created successfully - email: {}", accountVo.getEmail());
            SendMailInfo registrationMail = EmailBuilder.buildRegistrationMail(accountVo.getEmail());
            emailService.sendEmailToMany(registrationMail);
            return true;
        } catch (Exception e) {
            LOGGER.error("Unexpected error during registration process - email: {}", accountVo.getEmail(), e);
            return false;
        }
    }

    @Override
    public boolean verifyAccount(String token) {
        try {
            String email = TokenUtils.getEmailFromToken(token);
            AccountInfo accountInfo = findByEmail(email);
            if (accountInfo == null) {
                LOGGER.warn("Account verification failed - email not found: {}", email);
                return false;
            }
            if (accountInfo.getStatusId()==1) {
                LOGGER.warn("Account already verified - email: {}", email);
                return false;
            }
            accountInfo.setStatusId(Constant.STATUS_ACTIVE);
            accountRepository.save(accountInfo);
            LOGGER.info("Account verified successfully - email: {}", email);
            return true;
        } catch (Exception e) {
            LOGGER.error("Unexpected error during account verification process - token: {}", token, e);
            return false;
        }
    }

    @Override
    public Map<String, String> changePasswordHandle(String oldPassword, String newPassword, String confirmPassword) {
        AccountInfo account = getCurrentAccountInfo();
        Map<String, String> validateResult = validateService.validatePasswordChangeRules(oldPassword, newPassword, confirmPassword);
        if (!validateResult.isEmpty()) {
            return validateResult;
        }
        LOGGER.info("Password successfully updated for user");
        updatePassword(account, newPassword);
        return new HashMap<>();
    }
    public boolean forgotPasswordProcess(String email, Model model) {
        try {
            LOGGER.info("Forgot password process - email: {}", email);
            boolean isValidate = validateService.validateForgotPassword(email, model);
            AccountInfo accountInfo = accountRepository.findByEmail(email);
            if (isValidate || accountInfo == null) {
                LOGGER.warn("Forgot password validation failed - email not found: {}", email);
                return false;
            }
            SendMailInfo resetMail = EmailBuilder.buildForgotPasswordMail(email, accountInfo.getDatetimeChangePass());
            emailService.sendEmailToMany(resetMail);
            model.addAttribute("linkSendStatus", globalConfig.getSendResetPassword());
            return true;
        } catch (Exception e) {
            LOGGER.error("Unexpected error during forgot password process - email: {}", email, e);
            model.addAttribute(ERROR_ATTRIBUTE, globalConfig.getAnErrorOccur());
            return false;
        }
    }

    @Override
    public AccountInfo getCurrentAccountInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(email);
    }

    @Override
    public String resetPasswordProcess(String token, String newPassword, String confirmPassword, Model model) {
        LOGGER.info("Reset password process ");
        String email = TokenUtils.getEmailFromToken(token);
        AccountInfo account = findByEmail(email);
        model.addAttribute("token", token);
        if (account == null) {
            LOGGER.warn("Account not found - email: {}", email);
            model.addAttribute(ERROR_ATTRIBUTE, globalConfig.getEmailNotExist());
            return Constant.RESET_PASSWORD_PAGE;
        }
        if (!TokenUtils.isTokenValid(token, account)) {
            model.addAttribute(ERROR_ATTRIBUTE, globalConfig.getExpiredLink());
            LOGGER.warn("Token is invalid");
            return Constant.RESET_PASSWORD_PAGE;
        }
        boolean hasError = validateService.validateResetPassword(newPassword, confirmPassword, model);
        if (hasError) {
            LOGGER.warn("Reset password validation failed - email: {}", email);
            return Constant.RESET_PASSWORD_PAGE;
        } else {
            account.setDatetimeChangePass(Instant.now());
            updatePassword(email, newPassword);
            model.addAttribute("passwordReset", globalConfig.getPasswordResetSuccess());
            return Constant.RESET_PASSWORD_PAGE;
        }
    }

    /**
     * Handles the forgot password process.
     * Validates the email, checks if the account exists, and sends a password reset email.
     *
     * @param email The email address of the user requesting a password reset.
     * @return A map containing validation errors if any, otherwise an empty map.
     */
    @Override
    public Map<String, String> handleForgotPassword(String email) {
        // Log the start of the forgot password process
        LOGGER.debug("Handling forgot password for email: {}", email);
        // Validate the email and return errors if any
        Map<String, String> errors = validateService.validateForgotPassword(email);
        if (!errors.isEmpty()) {
            return errors;
        }
        // Check if an account exists for the given email
        AccountInfo accountInfo = accountRepository.findByEmail(email);
        if (accountInfo == null) {
            LOGGER.warn("No account found for email: {}", email);
            errors.put("emailError", globalConfig.getEmailNotExist());
            return errors;
        }
        // Build and send a password reset email
        SendMailInfo resetMail = EmailBuilder.buildForgotPasswordMail(email, accountInfo.getDatetimeChangePass());
        emailService.sendEmailToMany(resetMail);
        // Log the successful email sending
        LOGGER.info("Password reset email sent to: {}", email);
        // Return an empty map indicating success
        return new HashMap<>();
    }

    @Override
    public AccountInfo validateResetToken(String token, Model model) {
        String email = TokenUtils.getEmailFromToken(token);
        AccountInfo account = findByEmail(email);
        if (account == null || !TokenUtils.isTokenValid(token, account)) {
            model.addAttribute(ERROR_ATTRIBUTE, globalConfig.getExpiredLink());
            LOGGER.warn("Invalid or expired token");
            return null;
        }
        model.addAttribute("token", token);
        return account;
    }

    /**
     * Validates an account based on the given token.
     * Extracts the email from the token and checks if the token is valid.
     *
     * @param token The token to validate.
     * @return The associated AccountInfo if valid, otherwise null.
     */
    @Override
    public AccountInfo validateAccountToken(String token) {
        String email = TokenUtils.getEmailFromToken(token);
        AccountInfo account = findByEmail(email);
        if (account == null || !TokenUtils.isTokenValid(token, account)) {
            LOGGER.warn("Invalid or expired token");
            return null;
        }
        return account;
    }

    /**
     * Handles the password reset process.
     * Validates the token, checks password confirmation, and updates the password if valid.
     *
     * @param token           The token used for password reset.
     * @param newPassword     The new password entered by the user.
     * @param confirmPassword The confirmation of the new password.
     * @return A map containing validation errors if any, otherwise null if successful.
     */

    @Override
    public Map<String, String> handleResetPassword(String token, String newPassword, String confirmPassword) {
        AccountInfo account = validateAccountToken(token);
        if (account == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("tokenError", globalConfig.getExpiredLink());
            return errors;
        }
        Map<String, String> errors = validateService.validateResetPassword(newPassword, confirmPassword);
        if (!errors.isEmpty()) {
            LOGGER.warn("Reset password failed - email: {}", account.getEmail());
            return errors;
        }
        account.setDatetimeChangePass(Instant.now());
        updatePassword(account, newPassword);
        return new HashMap<>();
    }

    @Override
    public boolean resetPassword(String token, String newPassword, String confirmPassword, Model model) {
        AccountInfo account = validateResetToken(token, model);
        if (account == null) {
            return false;
        }
        boolean hasError = validateService.validateResetPassword(newPassword, confirmPassword, model);
        if (hasError) {
            LOGGER.warn("Reset password failed - email: {}", account.getEmail());
            return false;
        }
        account.setDatetimeChangePass(Instant.now());
        updatePassword(account.getEmail(), newPassword);
        return true;
    }

    @Override
    public boolean updateAccountDetails(AccountInfo accountInfo, Model model) {
        AccountInfo currentAccount = findByEmail(accountInfo.getEmail());
        if (currentAccount == null) {
            model.addAttribute(ERROR_ATTRIBUTE, globalConfig.getUserNotFound());
            return false;
        }
        boolean hasError = validateService.validateUpdateAccount(accountInfo, currentAccount.getPhone(), model);
        if (StringUtils.isEmpty(accountInfo.getAddress())) {
                accountInfo.setCity(currentAccount.getCity());
                accountInfo.setWard(currentAccount.getWard());
                accountInfo.setDistrict(currentAccount.getDistrict());
                accountInfo.setAddress(currentAccount.getAddress());
        }
        if (hasError) {
            return false;
        }
        updateAccountInfo(currentAccount, accountInfo);
        model.addAttribute("successMessage", globalConfig.getUpdateSuccess());
        return true;
    }
    @Override
    public Map<String,String> updateAccountProcess(AccountInfo accountInfo){
        AccountInfo currentAccount = getCurrentAccountInfo();
        Map<String,String>  updateAccountErrors = validateService.validateAccountField(
                accountInfo.getFullName(),accountInfo.getPhone(),
                currentAccount.getPhone(),accountInfo.getDob()
        );
        if (StringUtils.isEmpty(accountInfo.getAddress())) {
            accountInfo.setCity(currentAccount.getCity());
            accountInfo.setWard(currentAccount.getWard());
            accountInfo.setDistrict(currentAccount.getDistrict());
            accountInfo.setAddress(currentAccount.getAddress());
        }
        if(!updateAccountErrors.isEmpty()){
           return updateAccountErrors;
        }
        updateAccountInfo(currentAccount, accountInfo);
        return new HashMap<>();
    }
}
