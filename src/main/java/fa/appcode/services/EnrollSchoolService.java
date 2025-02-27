package fa.appcode.services;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface EnrollSchoolService {
    void enrollSchool(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role);
    EnrollSchool findEnrollSchoolById(Integer id);
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id, Pageable pageable);
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId,String schoolOwnerId,Pageable pageable);
    void unenrollParentToSchool(EnrollSchool enrollSchool, LocalDate unenrollDate, String role);
}
