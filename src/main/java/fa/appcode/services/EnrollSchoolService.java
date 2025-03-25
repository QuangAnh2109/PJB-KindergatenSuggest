package fa.appcode.services;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.MySchoolVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface EnrollSchoolService {
    EnrollSchool findEnrollSchoolById(Integer id);

    /**
     * This method to Find All Enrolled School Of School Owner That Parent Enrolled with Parent ID and SchoolOwner email
     *
     * @param parentId
     * @param schoolOwnerEmail
     * @param pageable
     * @return Page<EnrolledSchoolVo>
     */
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerEmail, Pageable pageable);

    //Execute Unenroll Parent to school
    /**
     * This method to evaluate to unenroll parent to school
     *
     * @param enrollSchool
     * @param enrollEndDate
     * @param role
     * @param status
     * @param email
     * @param recordNo
     */
    void evaluateParentEnroll(EnrollSchool enrollSchool, LocalDate enrollEndDate, String role, Integer status, String email, Integer recordNo, Integer parentId);

    //Enroll Parent School
    /**
     * This method to enroll Parent to school
     *
     * @param account
     * @param school
     * @param enrollDate
     * @param schoolOwnerEmail
     */
    void enrollSchoolParent(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role, String schoolOwnerEmail);

    //check if parent is already enrolled or not
    /**
     * This method to check if parent is already enrolled to school or not
     *
     * @param parentId
     * @param schoolId
     * @return true/false
     */
    boolean isEnrolled(Integer parentId, Integer schoolId);

    //validate school Owner Access
    /**
     * This method to check if school owner have access to school
     *
     * @param schoolId
     * @param schoolOwnerEmail
     * @return true/false
     */
    boolean validateAccess(Integer schoolId, String schoolOwnerEmail);

    //Get list Parent's School that is enrolled
    /**
     * This method to get list school that parent has enrolled by ParentId
     *
     * @param parentId
     * @param pageable
     * @return list schoolInfo
     */
    Page<MySchoolVo> findListSchoolParentEnrolledByParentId(int parentId, Pageable pageable);


    //Get list Parent's School that is previous enrolled
    /**
     * This method to get list school that parent has un-enrolled by ParentId(the school that has enrolled before)
     * @param parentId
     * @param pageable
     * @return list schoolInfo
     */
    Page<MySchoolVo> findListSchoolParentPreEnrolledByParentId(int parentId,Pageable pageable);

    Map<Integer, List<String>> getFacilitiesMapForSchools(Page<MySchoolVo> schoolPage);

}
