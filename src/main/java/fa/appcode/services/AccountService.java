package fa.appcode.services;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.common.vo.RoleVo;
import fa.appcode.entities.AccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    Page<ParentVo> findAllParent(String search ,Pageable pageable);
    ParentVo findParentById(int id);
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id,Pageable pageable);
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId,String schoolOwnerId,Pageable pageable);
    RoleVo findByEmail(String email);
    AccountInfo getAccountInfoById(int id);
}
