package fa.appcode.repositories;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.EnrollSchool;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("enrollSchoolRepository")
public interface EnrollSchoolRepository extends JpaRepository<EnrollSchool, Integer> {
    EnrollSchool findEnrollSchoolById(Integer id);

    //    Find All enrolled School for specific School Owner
    @Query("SELECT new fa.appcode.common.vo.EnrolledSchoolVo(e.id,s.schoolName,CAST(ceiling(((f.extracurricularActivities + f.facilitiesUtilities + f.hygieneNutrition + f.learningProgram + f.teacherStaff) / 5) * 2) / 2 AS FLOAT),f.feedbackMessage)" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.id " +
            "JOIN  EnrollSchool e ON e.account.id=ai.id " +
            "JOIN SchoolInfo s ON s.id=e.school.id " +
            "LEFT JOIN Feedback f on f.id.schoolId=s.id AND f.id.accountId=ai.id AND " +
            "f.id.feedbackTime = ( " +
            "          SELECT MAX(f2.id.feedbackTime)" +
            "          FROM Feedback f2 " +
            "          WHERE f2.id.schoolId = s.id " +
            "         AND f2.id.accountId = ai.id AND f2.deleteFlg=false) " +
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=1 AND s.account.email=?2 AND e.status=3")
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner (int parentId, String schoolOwnerId, Pageable pageable);

    //    Find All enrolled School for admin
    @Query("SELECT new fa.appcode.common.vo.EnrolledSchoolVo(e.id,s.schoolName,CAST(ceiling(((f.extracurricularActivities + f.facilitiesUtilities + f.hygieneNutrition + f.learningProgram + f.teacherStaff) / 5) * 2) / 2 AS FLOAT),f.feedbackMessage)" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.id " +
            "JOIN  EnrollSchool e ON e.account.id=ai.id " +
            "JOIN SchoolInfo s ON s.id=e.school.id " +
            "LEFT JOIN Feedback f on f.id.schoolId=s.id AND f.id.accountId=ai.id AND " +
            "f.id.feedbackTime = ( " +
            "          SELECT MAX(f2.id.feedbackTime)" +
            "          FROM Feedback f2 " +
            "          WHERE f2.id.schoolId = s.id " +
            "         AND f2.id.accountId = ai.id AND f2.deleteFlg=false) " +
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=1 AND e.status=3")
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId (int id,Pageable pageable);

    //    Find All Request enroll School for specific School Owner
    @Query("SELECT new fa.appcode.common.vo.EnrolledSchoolVo(e.id,s.schoolName)" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.id " +
            "JOIN  EnrollSchool e ON e.account.id=ai.id " +
            "JOIN SchoolInfo s ON s.id=e.school.id " +
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=1 AND s.account.email=?2 AND e.status=1")
    List<EnrolledSchoolVo> findParentRequestEnrollSchoolByParentIdAndSchoolOwner (int parentId, String schoolOwnerId);

    //    Find All enrolled School for admin
    @Query("SELECT new fa.appcode.common.vo.EnrolledSchoolVo(e.id,s.schoolName)" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.id " +
            "JOIN  EnrollSchool e ON e.account.id=ai.id " +
            "JOIN SchoolInfo s ON s.id=e.school.id " +
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=1 AND e.status=1")
    List<EnrolledSchoolVo> findParentRequestEnrolledSchoolByParentId (int id);

}
