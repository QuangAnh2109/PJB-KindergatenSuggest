package fa.appcode.services;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface EnrollSchoolService {
    EnrollSchool findEnrollSchoolById(Integer id);

    //Find All School that parent Enrolled with ParentID
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id, Pageable pageable);

    //Find All Enrolled School Of School Owner That Parent Enrolled with Parent ID and SchoolOwner email
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId, Pageable pageable);

    //Execute Unenroll Parent to school
    void evaluateParentEnroll(EnrollSchool enrollSchool, LocalDate approvalEnrollDate, String role, Integer status, Principal principal, Integer recordNo) throws Exception;

    //find All Parent Request Enroll for Specific School Owner
    List<EnrolledSchoolVo> findParentRequestEnrollSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId);

    //find All Parent Request Enroll for Admin
    List<EnrolledSchoolVo> findParentRequestEnrolledSchoolByParentId(int id);

    //Enroll Parent School
    void enrollSchoolParent(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role, Principal principal) throws Exception;

    //check if parent is already enrolled or not
    String isEnrolled(Integer parentId, Integer schoolId);

    //validate school Owner Access
    String validateAccess(Integer schoolId, Principal principal);

}
