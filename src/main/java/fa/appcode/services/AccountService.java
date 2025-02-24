package fa.appcode.services;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.entities.AccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    Page<AccountInfo> findAll(Pageable pageable);
    List<AccountInfo> findAllRoles();
    Page<ParentVo> findAllParent(Pageable pageable);
    ParentVo findParentById(int id);
    Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id,Pageable pageable);
    AccountInfo findByEmail(String email);
}
