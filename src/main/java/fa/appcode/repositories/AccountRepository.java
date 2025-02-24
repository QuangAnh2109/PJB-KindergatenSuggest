package fa.appcode.repositories;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.entities.AccountInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("accountRepository")
@Transactional
public interface AccountRepository extends JpaRepository <AccountInfo,Integer>{
    /**
     * @param pageable
     * @return
     */

    @Query("select m from AccountInfo m join MasterDatum ma ON m.roleId=ma.typeKey WHERE ma.typeName='ROLE' and ma.typeKey=3")
    Page<AccountInfo> findAll(Pageable pageable);

    @Query("select m from AccountInfo m join MasterDatum ma ON m.roleId=ma.typeKey WHERE ma.typeName='ROLE' and ma.typeKey=3")
    List<AccountInfo> findAllRole();

    @Query("SELECT new fa.appcode.common.vo.ParentVo(m.id,m.fullName,m.email,m.phone,m.dob,CONCAT(m.address, ' - ', CONCAT(w.wardName, ' - ', CONCAT(d.districtName, ' - ', c.cityName))))" +
            "FROM AccountInfo m " +
            "JOIN MasterDatum ma ON m.roleId=ma.id " +
            "LEFT JOIN Ward w ON w.id=m.ward.id " +
            "LEFT JOIN District d ON d.id = m.district.id " +
            "LEFT JOIN City c ON c.id=m.city.id " +
            "WHERE m.id=?1")
    ParentVo findParentById (int id);

    @Query("SELECT new fa.appcode.common.vo.EnrolledSchoolVo(s.schoolName,CAST((f.extracurricularActivities + f.facilitiesUtilities + f.hygieneNutrition + f.learningProgram + f.teacherStaff)/5 AS FLOAT),f.feedbackMessage)" +
            "FROM AccountInfo m " +
            "JOIN MasterDatum ma ON m.roleId=ma.id " +
            "LEFT JOIN  EnrollSchool e ON e.account.id=m.id " +
            "LEFT JOIN SchoolInfo s ON s.id=e.school.id " +
            "LEFT JOIN Feedback f on f.id.schoolId=s.id AND f.id.accountId=m.id AND " +
            "f.id.feedbackTime = ( " +
            "          SELECT MAX(f2.id.feedbackTime)" +
            "          FROM Feedback f2 " +
            "          WHERE f2.id.schoolId = s.id " +
            "         AND f2.id.accountId = m.id) " +
            "WHERE m.id=?1 ")
    Page<EnrolledSchoolVo> findParentEnrolledSchoolBy (int id,Pageable pageable);

    @Query("SELECT new fa.appcode.common.vo.ParentVo(m.id,m.fullName,m.email,m.phone,(CASE WHEN EXISTS (SELECT e FROM EnrollSchool e WHERE e.account.id = m.id AND e.status != false) THEN true ELSE false END))" +
            "FROM AccountInfo m " +
            "JOIN MasterDatum ma ON m.roleId=ma.typeKey " +
            "LEFT JOIN  EnrollSchool e ON e.account.id=m.id " +
            "LEFT JOIN SchoolInfo s ON s.id=e.school.id " +
            "WHERE ma.typeName='ROLE' AND ma.typeKey=3 " +
            "GROUP BY m.id, m.fullName, m.email, m.phone")
    Page<ParentVo> findAllParent(Pageable pageable);
}
