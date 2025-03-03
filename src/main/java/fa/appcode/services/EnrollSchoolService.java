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
    //Execute Update Approve Reject Unenroll Parent to school
    void evaluateParentEnroll(EnrollSchool enrollSchool, LocalDate approvalEnrollDate, String role, Integer status);
    //Call to Update Approve, Reject, Unenroll
    String execute(String action, EnrollSchool enrollSchool, LocalDate date, String role, Principal principal) throws Exception;
    //find All Parent Request Enroll for Specific School Owner
    List<EnrolledSchoolVo> findParentRequestEnrollSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId);
    //find All Parent Request Enroll for Admin
    List<EnrolledSchoolVo> findParentRequestEnrolledSchoolByParentId(int id);
}
