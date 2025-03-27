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
import fa.appcode.exceptions.TokenException;
import fa.appcode.exceptions.ValidateParentException;
import fa.appcode.exceptions.ValidationException;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.repositories.MasterDatumRepository;
import fa.appcode.services.*;
import jakarta.transaction.Transactional;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import fa.appcode.services.MasterDatumService;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    @Autowired
    private MasterDatumRepository masterDatumRepository;
    @Autowired
    private AccountRepository accountRepository;
    private final ValidateService validateService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
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
                .orElseThrow(() -> new EntityNotFoundException(globalConfig.getUserNotFound()));

        AccountVo accountVo = new AccountVo();
        accountVo.setId(accountInfo.getId());
        accountVo.setFullName(accountInfo.getFullName());
        accountVo.setEmail(accountInfo.getEmail());
        accountVo.setPhone(accountInfo.getPhone());
        accountVo.setDob(accountInfo.getDob() != null ? accountInfo.getDob().toString() : null);
        // Resolve role and status names
        accountVo.setRole(masterDatumRepository.getMasterByTypeNameAndTypeKey("ROLE", accountInfo.getRoleId()));
        accountVo.setStatus(masterDatumRepository.getMasterByTypeNameAndTypeKey("ACCOUNT STATUS", accountInfo.getStatusId()));
        accountVo.setRecordNo(accountInfo.getRecordNo());
        return accountVo;
    }


    // Update user account
    @Override
    public int updateAccount(AccountVo accountVo) {
        AccountInfo user = accountRepository.findById(accountVo.getId())
                .orElseThrow(() -> new EntityNotFoundException(globalConfig.getUserNotFound()));

        LOGGER.info("recordNo get in DB : {}", user.getRecordNo());

        if (!user.getRecordNo().equals(accountVo.getRecordNo())) {
            throw new IllegalStateException(globalConfig.getInvalidRecordNo());
        }

        // check data has changes or not
        boolean isModified = false;

        if (!Objects.equals(user.getRoleId(), masterDatumRepository.getMasterKeyByTypeNameAndTypeValue("ROLE", accountVo.getRole()))) isModified = true;
        if (!Objects.equals(user.getStatusId(), masterDatumRepository.getMasterKeyByTypeNameAndTypeValue("ACCOUNT STATUS", accountVo.getStatus()))) isModified = true;

        if (!isModified) {
            throw new IllegalStateException("No changes detected, update aborted.");
        }

        // if has changes, perform update
        user.setRoleId(masterDatumRepository.getMasterKeyByTypeNameAndTypeValue("ROLE", accountVo.getRole()));
        user.setStatusId(masterDatumRepository.getMasterKeyByTypeNameAndTypeValue("ACCOUNT STATUS", accountVo.getStatus()));
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
                .orElseThrow(() -> new EntityNotFoundException(globalConfig.getUserNotFound()));
        account.setDeleteFlg(true);
        accountRepository.save(account);
    }

    @Override
    public void addUserFromAdmin(AccountVo accountVo, Principal principal) {
        // Validate accountVo
        Map<String, String> errors = validateService.validateAccountVo(accountVo);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // Generate password by system
        String randomPassword = UUID.randomUUID().toString();
        accountVo.setPassword(randomPassword);
        accountVo.setConfirmPassword(randomPassword);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFullName(accountVo.getFullName());
        accountInfo.setEmail(accountVo.getEmail());
        accountInfo.setPhone(accountVo.getPhone());
        accountInfo.setDob(LocalDate.parse(accountVo.getDob()));
        accountInfo.setRoleId(masterDatumRepository.getMasterKeyByTypeNameAndTypeValue("ROLE", accountVo.getRole()));
        accountInfo.setPassword(encodePassword(accountVo.getPassword()));
        accountInfo.setStatusId(masterDatumRepository.getMasterKeyByTypeNameAndTypeValue("ACCOUNT STATUS", accountVo.getStatus())); // Default status
        accountInfo.setImageUrl("null");
        accountInfo.setRecordNo(1);
        accountInfo.setCreateId("SYSTEM_ADMIN");
        accountInfo.setUpdateId("SYSTEM_ADMIN");
        accountInfo.setCreateTime(Instant.now());
        accountInfo.setUpdateTime(Instant.now());
        accountRepository.save(accountInfo);

        // Send email
        String ownerName = this.getAccountInfo(principal).getFullName();
        SendMailInfo sendMailInfo = EmailBuilder.buildAddUserMail(accountVo.getEmail(), randomPassword, ownerName);
        emailService.sendEmailToMany(sendMailInfo);
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
    public int getAccountIdByEmail(String email) {
        return accountRepository.findAccountByEmailAndStatusIdAndDeleteFlg(email, 1, false).getId();
    }

    @Override
    public String getAccountNameByEmailAndNoDelete(String email) {
        return accountRepository.getAccountNameByEmailAndDeleteFlg(email, false);
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

    /**
     * Handles the registration process for a new user.
     * @param accountVo The account information provided by the user.
     * @return A map containing validation errors if any exist; otherwise, an empty map indicating success.
     */
    @Override
    public Map<String, String> handleRegisterProcess(AccountVo accountVo) {
        // Validate the input fields (full name, email, phone, password, confirm password)
        Map<String, String> validationResult = validateService.registerValidation(
                accountVo.getFullName(), accountVo.getEmail(), accountVo.getPhone(),
                accountVo.getPassword(), accountVo.getConfirmPassword()
        );

        // Check if there are any validation errors
        if (!validationResult.isEmpty()) {
            // Log the validation failure with the specific errors
            LOGGER.warn("Validation failed: {}", validationResult);
            // Return the validation errors so they can be displayed to the user
            return validationResult;
        }
        // If validation passes, create a new account and store it in the database
        createAccount(accountVo);
        // Log a message indicating that the account was created successfully
        LOGGER.info("Account created successfully - email: {}", accountVo.getEmail());
        // Send a registration confirmation email to the user
        emailService.sendEmailToMany(EmailBuilder.buildRegistrationMail(accountVo.getEmail()));
        // Return an empty map, indicating that registration was successful
        return Collections.emptyMap();
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
            if (accountInfo.getStatusId() == 1) {
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
    /**
     * Handles the password change process for the currently logged-in user.
     * @param oldPassword      The current password of the user.
     * @param newPassword      The new password entered by the user.
     * @param confirmPassword  The confirmation of the new password entered by the user.
     * @return A map where keys represent field names and values contain validation error messages.
     *         Returns an empty map if the password is successfully updated.
     */
    @Override
    public Map<String, String> changePasswordHandle(String oldPassword, String newPassword, String confirmPassword) {
        // Retrieve the currently logged-in user's account information
        AccountInfo account = getCurrentAccountInfo();
        // Validate the password change rules
        Map<String, String> validateResult = validateService.validatePasswordChangeRules(oldPassword, newPassword, confirmPassword);
        // If there are validation errors, return them
        if (!validateResult.isEmpty()) {
            return validateResult;
        }
        // Log successful password update
        LOGGER.info("Password successfully updated for user");
        // Update the password in the database
        updatePassword(account, newPassword);
        // Return an empty map indicating success
        return Collections.emptyMap();
    }


    @Override
    public AccountInfo getCurrentAccountInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(email);
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
        LOGGER.debug("Handling forgot password for email: {}", email);
        Map<String, String> errors = validateService.validateForgotPassword(email);
        if (!errors.isEmpty()) {
            return errors;
        }
        AccountInfo accountInfo = accountRepository.findByEmail(email);
        if (accountInfo == null || accountInfo.getDeleteFlg()) {
            LOGGER.warn("No account found for email: {}", email);
            return Map.of("emailError", globalConfig.getEmailNotExist());
        } else if (accountInfo.getStatusId()!=1) {
            LOGGER.warn("Account is not active - email: {}", email);
            return Map.of("emailError", globalConfig.getAccountNotActive());
        }
        // Build and send a password reset email
        SendMailInfo resetMail = EmailBuilder.buildForgotPasswordMail(email, accountInfo.getDatetimeChangePass());
        emailService.sendEmailToMany(resetMail);
        LOGGER.info("Password reset email successfully sent to: {}", email);
        return Collections.emptyMap();
    }

    @Override
    public boolean isValidAccountToken(String token) {
        try {
            return TokenUtils.isTokenValid(token, findByEmail(TokenUtils.getEmailFromToken(token)));
        } catch (TokenException e) {
            LOGGER.warn("Invalid or expired token: {}", e.getMessage());
            return false;
        }
    }
    /**
     * Handles the password reset process.
     * Validates the token, checks password confirmation, and updates the password if valid.
     *
     * @param token           The token used for password reset.
     * @param newPassword     The new password entered by the user.
     * @param confirmPassword The confirmation of the new password.
     * @return A map containing validation errors if any, otherwise empty map if successful.
     */

    @Override
    public Map<String, String> handleResetPassword(String token, String newPassword, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();
        if (!isValidAccountToken(token)) {
            errors.put("tokenError", globalConfig.getExpiredLink());
            return errors;
        }
        String email = TokenUtils.getEmailFromToken(token);
        AccountInfo account = findByEmail(email);
        errors.putAll(validateService.validateResetPassword(newPassword, confirmPassword));
        if (!errors.isEmpty()) {
            LOGGER.warn("Reset password failed - email: {}", account.getEmail());
            return errors;
        }
        updatePassword(account, newPassword);
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> updateAccountProcess(AccountInfo accountInfo) {
        // Retrieve the current account information of the logged-in user
        AccountInfo currentAccount = getCurrentAccountInfo();
        if(!currentAccount.getRecordNo().equals( accountInfo.getRecordNo())) {
            LOGGER.info("Record no not equal to current account record no: {}", accountInfo.getRecordNo());
            return Map.of("recordChange","Record Not Match");
        }
        // Validate the updated account fields
        Map<String, String> validationResult = validateService.validateAccountField(
                accountInfo.getFullName(), accountInfo.getPhone(),
                currentAccount.getPhone(), accountInfo.getDob()
        );
        // If there are validation errors, return them immediately
        if (!validationResult.isEmpty()) {
            return validationResult;
        }
        // Retain the existing address details if they are not provided in the updated data
        retainExistingAddressIfEmpty(accountInfo, currentAccount);
        // Update the current account information with the new details
        updateAccountInfo(currentAccount, accountInfo);
        // Return an empty map indicating a successful update
        return Collections.emptyMap();
    }

    /**
     * Ensures that if an address field in the updated account information is empty,
     * it retains the corresponding value from the current account.
     */
    private void retainExistingAddressIfEmpty(AccountInfo accountInfo, AccountInfo currentAccount) {
        accountInfo.setCity(Optional.ofNullable(accountInfo.getCity()).orElse(currentAccount.getCity()));
        accountInfo.setWard(Optional.ofNullable(accountInfo.getWard()).orElse(currentAccount.getWard()));
        accountInfo.setDistrict(Optional.ofNullable(accountInfo.getDistrict()).orElse(currentAccount.getDistrict()));
        accountInfo.setAddress(Optional.ofNullable(accountInfo.getAddress()).orElse(currentAccount.getAddress()));
    }


    @Override
    public List<String> getAllAccountEmailsByRole(int roleId) {
        return accountRepository.getAllEmailByRoleAndDeleteFlg(roleId, false);
    }
}
