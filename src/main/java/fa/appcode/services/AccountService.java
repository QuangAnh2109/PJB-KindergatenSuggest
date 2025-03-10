package fa.appcode.services;

import fa.appcode.common.vo.AccountVo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.entities.AccountInfo;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public interface AccountService {

    String encodePassword(String password);

    void save(AccountInfo accountInfo);

    String processRegister(AccountVo accountVo, BindingResult bindingResult, Model model);

    String verifyAccount(String token, Model model);

    AccountInfo findByEmail(String email);


    AccountInfo findAccountInfoByPhone(String phone);

    boolean updatePassword(String email, String newPassword);

    Page<ParentVo> findAllParent(String search, Pageable pageable);

    //tuanpa79

    /**
     * Retrieves a paginated list of user accounts based on search criteria.
     *
     * @param search
     * @param pageable t
     * @return a page of AccountVo containing user account details
     */
    Page<AccountVo> getAllAccounts(String search, Pageable pageable);

    /**
     * Retrieves a user account based on the provided ID.
     *
     * @param id
     * @return the AccountVo containing user account details
     */
    AccountVo getAccountById(Integer id);

    /**
     * Changes the status of a user account (e.g., activates or deactivates the account).
     *
     * @param id
     */
    void toggleUserStatus(Integer id);

    /**
     * Updates the details of an existing user account.
     *
     * @param accountVo
     */
    void updateUser(AccountVo accountVo);

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
    void addUserFromAdmin(AccountVo accountVo);


    Page<AccountInfo> findAll(Pageable pageable);

    List<AccountInfo> findAllRoles();

    AccountInfo createAccount(AccountVo accountVo);

    Page<ParentVo> findAllParent(Pageable pageable);

    ParentVo findParentById(int id);

    String findAccountRoleString(String email);

    AccountInfo getAccountInfoById(int id);

    Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id, Pageable pageable);


    Page<ParentVo> findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(String email, String search, Pageable pageable);

    AccountInfo getAccountInfo(Principal principal);

    String getEmailByAccountIdAndActiveAndNoDelete(int accountId);

    String getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(int id);

    void updateAccountInfo(AccountInfo existing, AccountInfo formData);

    void saveAccountInfo(AccountInfo accountInfo);

    String changePasswordProcess(String oldPassword, String newPassword, String confirmPassword, Model model);

    String forgotPasswordProcess(String email, Model model);

    String resetPasswordProcess(String token, String newPassword, String confirmPassword, Model model);
    AccountInfo validateResetToken(String token, Model model);
    String resetPassword(String token, String newPassword, String confirmPassword, Model model);
    public String updateAccountDetails(AccountInfo accountInfo, Model model);
    AccountInfo getCurrentAccountInfo();

}
