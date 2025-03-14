package fa.appcode.repositories;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository("enrollSchoolRepository")
public interface EnrollSchoolRepository extends JpaRepository<EnrollSchool, Integer> {
    @Query("""
            SELECT e FROM EnrollSchool e WHERE e.id = ?1 AND e.deleteFlg=false AND e.status != 4
            """)
    EnrollSchool findEnrollSchoolById(Integer id);

    //    Find All enrolled School for specific School Owner
    @Query("""
            SELECT new fa.appcode.common.vo.EnrolledSchoolVo(e.id,s.schoolName,CAST(ceiling(((f.extracurricularActivities + f.facilitiesUtilities + f.hygieneNutrition + f.learningProgram + f.teacherStaff) / 5) * 2) / 2 AS FLOAT),f.feedbackMessage,e.recordNo)
                        FROM AccountInfo ai 
                        JOIN MasterDatum ma ON ai.roleId=ma.id 
                        JOIN  EnrollSchool e ON e.account.id=ai.id 
                        JOIN SchoolInfo s ON s.id=e.school.id 
                        LEFT JOIN Feedback f on f.id.schoolId=s.id AND f.id.accountId=ai.id AND 
                        f.id.feedbackTime = ( 
                                 SELECT MAX(f2.id.feedbackTime)
                                 FROM Feedback f2 
                                 WHERE f2.id.schoolId = s.id 
                                 AND f2.id.accountId = ai.id AND f2.deleteFlg=false) 
                        WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=1 AND s.account.email=?2 AND e.status=3""")
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId, Pageable pageable);

    //    Find All enrolled School for admin
    @Query("""
            SELECT new fa.appcode.common.vo.EnrolledSchoolVo(e.id,s.schoolName,CAST(ceiling(((f.extracurricularActivities + f.facilitiesUtilities + f.hygieneNutrition + f.learningProgram + f.teacherStaff) / 5) * 2) / 2 AS FLOAT),f.feedbackMessage,e.recordNo)
                        FROM AccountInfo ai 
                        JOIN MasterDatum ma ON ai.roleId=ma.id
                        JOIN  EnrollSchool e ON e.account.id=ai.id 
                        JOIN SchoolInfo s ON s.id=e.school.id
                        LEFT JOIN Feedback f on f.id.schoolId=s.id AND f.id.accountId=ai.id AND 
                        f.id.feedbackTime = ( 
                                  SELECT MAX(f2.id.feedbackTime)
                                  FROM Feedback f2 
                                  WHERE f2.id.schoolId = s.id 
                                 AND f2.id.accountId = ai.id AND f2.deleteFlg=false)
                        WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=1 AND e.status=3""")
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id, Pageable pageable);

    //    Find All enrolled School for admin
    @Query("""
            SELECT new fa.appcode.common.vo.EnrolledSchoolVo(e.id,s.schoolName,e.recordNo)
                        FROM AccountInfo ai
                        JOIN MasterDatum ma ON ai.roleId=ma.id
                        JOIN  EnrollSchool e ON e.account.id=ai.id
                        JOIN SchoolInfo s ON s.id=e.school.id
                        WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=1 AND e.status=1""")
    List<EnrolledSchoolVo> findParentRequestEnrolledSchoolByParentId(int id);

    //Check If Parent is Enrolled Or Not
    @Query("""
            SELECT COUNT(e)>0 FROM EnrollSchool e
            WHERE e.account.id=?1
            AND e.school.id=?2 AND e.status=3
            AND e.deleteFlg=false
            """)
    boolean isParentEnrollingToSchool(Integer accountId, Integer schoolId);

    @Modifying
    @Query("""
            UPDATE EnrollSchool e SET e.enrollEndDate = :enrollEndDate,
                        e.updateId = :updateId, e.updateTime = :updateTime, e.status = :status,
                        e.recordNo = e.recordNo + 1 
                        WHERE e.id = :id AND e.recordNo = :currentRecordNo AND e.deleteFlg=false""")
    @Transactional
    int evaluateParentEnroll(@Param("id") Integer id,
                           @Param("enrollEndDate") LocalDate enrollEndDate,
                           @Param("updateId") String updateId,
                           @Param("updateTime") Instant updateTime,
                           @Param("status") Integer status,
                           @Param("currentRecordNo") Integer currentRecordNo);
}
