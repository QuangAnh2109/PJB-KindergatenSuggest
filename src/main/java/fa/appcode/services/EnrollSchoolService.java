package fa.appcode.services;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.LocalDate;

public interface EnrollSchoolService {
    void enrollSchoolParent(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role);

    EnrollSchool findEnrollSchoolById(Integer id);

    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id, Pageable pageable);

    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId, Pageable pageable);

    void unenrollParentToSchool(EnrollSchool enrollSchool, LocalDate unenrollDate, String role);

    void evaluateParentEnroll(EnrollSchool enrollSchool, LocalDate approvalEnrollDate, String role, Integer status);

    void execute(String action, EnrollSchool enrollSchool, LocalDate date, String role, Principal principal) throws Exception;
}
