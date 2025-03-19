package fa.appcode.services;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.exceptions.ValidateParentException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.entities.AccountInfo;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public interface AccountService {

    String encodePassword(String password);

    void save(AccountInfo accountInfo);

    AccountInfo findByEmail(String email);

    AccountInfo findAccountInfoByPhone(String phone);

    boolean updatePassword(String email, String newPassword);

    /**
     * Retrieves a paginated list of parent based on the search
     *
     * @param search
     * @param pageable
     * @return a page of ParentVo containing user account details
     */
    Page<ParentVo> findAllParent(String search, Pageable pageable);

    //tuanpa79

    /**
     * Retrieves a paginated list of user accounts based on search criteria.
     *
     * @param search
     * @param pageable t
     * @return a page of AccountVo containing user account details
     */
    Page<AccountVo> getAllAccounts(String search, Pageable pageable) throws Exception;

    /**
     * Retrieves a user account based on the provided ID.
     *
     * @param id
     * @return the AccountVo containing user account details
     */
    AccountVo getAccountById(Integer id);

    /**
     * Updates the status or role of an existing user account.
     *
     * @param accountVo
     */
    int updateAccount(AccountVo accountVo);

    /**
     * Deletes logic a user account based on the provided ID. (set deleteFlg=1)
     *
     * @param id
     */
    void deleteAccount(Integer id);

    /**
     * Adds a new user account by admin
     *
     * @param accountVo
     */
    void addUserFromAdmin(AccountVo accountVo, Principal principal);

    AccountInfo createAccount(AccountVo accountVo);

    Page<ParentVo> findAllParent(Pageable pageable);

    /**
     * This method is used to find Parent based on their ID
     *
     * @param id
     * @return String role
     */
    ParentVo findParentById(int id) throws ValidateParentException;

    /**
     * This method if used to find role of account by using account email
     *
     * @param email
     * @return String role
     */
    String findAccountRoleString(String email);

    AccountInfo getAccountInfoById(int id);

    Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id, Pageable pageable);

    /**
     * Retrieves a paginated list of user accounts based on search and email of School Owner criteria.
     *
     * @param email
     * @param search
     * @param pageable t
     * @return a page of ParentVo containing user account details
     */
    Page<ParentVo> findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(String email, String search, Pageable pageable);

    AccountInfo getAccountInfo(Principal principal);

    String getEmailByAccountIdAndActiveAndNoDelete(int accountId);

    String getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(int id);

    void updateAccountInfo(AccountInfo existing, AccountInfo formData);

    Map<String, String> changePasswordHandle(String oldPassword, String newPassword, String confirmPassword);

    AccountInfo getCurrentAccountInfo();

    boolean verifyAccount(String token);


    public int getAccountIdByEmail(String email);
     boolean isValidAccountToken(String token);
    Map<String, String> handleForgotPassword(String email);

    Map<String, String> handleResetPassword(String token, String newPassword, String confirmPassword);

    Map<String, String> updateAccountProcess(AccountInfo accountInfo);

    Map<String, String> handleRegisterProcess(AccountVo accountVo);

}
