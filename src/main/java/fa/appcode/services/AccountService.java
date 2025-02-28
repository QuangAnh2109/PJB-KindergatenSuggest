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
import fa.appcode.common.vo.RoleVo;
import fa.appcode.entities.AccountInfo;


public interface AccountService {
     AccountInfo getAccountById(int id);
    boolean existsByEmail(String email);

    String encodePassword(String password);

    void save(AccountInfo accountInfo);

    AccountVo findAccountByEmail(String email);
    AccountInfo findByEmail(String email);
     void updateAccountInfo(AccountInfo accountInfo) ;
    AccountInfo findAccountInfoByPhone(String phone);
     boolean updatePassword(String email, String newPassword);

    Page<ParentVo> findAllParent(String search ,Pageable pageable);
    //tuanpa79
    Page<AccountVo> getAllAccounts(String search, Pageable pageable);

    AccountVo getAccountById(Integer id);
    void toggleUserStatus(Integer id);
    void updateUser(Integer id, String fullName, String phone, String dob, Integer roleId);
    void deleteAccount(Integer id);

    Page<AccountInfo> findAll(Pageable pageable);

    List<AccountInfo> findAllRoles();

    Page<ParentVo> findAllParent(Pageable pageable);

    ParentVo findParentById(int id);

    String findAccountRoleString(String email);
    AccountInfo getAccountInfoById(int id);

    Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id, Pageable pageable);
    AccountVo findAccountByPhone(String phone);

    Page<ParentVo> findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(String email,String search, Pageable pageable);

    AccountInfo getAccountInfo(Principal principal);
}
