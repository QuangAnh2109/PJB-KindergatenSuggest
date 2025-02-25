package fa.appcode.services;

import fa.appcode.common.vo.AccountVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.entities.AccountInfo;



public interface AccountService {
 boolean existsByEmail(String email);
 String encodePassword(String password);
 void save(AccountInfo accountInfo);
 AccountInfo findByEmail(String email);
 public boolean updatePassword(String email, String newPassword);
    Page<AccountVo> getAllAccounts(String search, Pageable pageable);
    AccountVo getAccountById(Integer id);
    Page<AccountInfo> findAll(Pageable pageable);
    List<AccountInfo> findAllRoles();
    Page<ParentVo> findAllParent(Pageable pageable);
    ParentVo findParentById(int id);
    Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id,Pageable pageable);
}
