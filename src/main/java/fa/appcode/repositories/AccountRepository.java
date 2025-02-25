package fa.appcode.repositories;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.common.vo.RoleVo;
import fa.appcode.entities.AccountInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository("accountRepository")
@Transactional
public interface AccountRepository extends JpaRepository <AccountInfo,Integer>{

    @Query("SELECT c FROM AccountInfo c WHERE c.email = ?1")
    AccountInfo findByEmail(String email);
    @Query("Select c from AccountInfo c where c.phone=?1")
    AccountVo findByPhone(String phone);
    @Modifying
    @Transactional
    @Query("UPDATE AccountInfo a SET a.password = ?1 WHERE a.email = ?2")
    int updatePassword(String newPassword, String email);


    @Query("""
    SELECT ai FROM AccountInfo ai
    LEFT JOIN FETCH ai.ward w
    LEFT JOIN FETCH ai.district d
    LEFT JOIN FETCH ai.city c
    WHERE ai.deleteFlg = false
    AND (:search IS NULL OR ai.fullName LIKE %:search% OR ai.email LIKE %:search% OR ai.phone LIKE %:search%)
""")
    Page<AccountInfo> findAllWithFullAddress(@Param("search") String search, Pageable pageable);

    /**
     * @param pageable
     * @return
     */

    @Query("select m from AccountInfo m join MasterDatum ma ON m.roleId=ma.typeKey WHERE ma.typeName='ROLE' and ma.typeKey=3")
    Page<AccountInfo> findAll(Pageable pageable);

    @Query("select m from AccountInfo m join MasterDatum ma ON m.roleId=ma.typeKey WHERE ma.typeName='ROLE' and ma.typeKey=3")
    List<AccountInfo> findAllRole();

    // Find Parent data by parent Id
    @Query("SELECT new fa.appcode.common.vo.ParentVo(ai.id,ai.fullName,ai.email,ai.phone,ai.dob,CONCAT(ai.address, ' - ', CONCAT(w.wardName, ' - ', CONCAT(d.districtName, ' - ', c.cityName))))" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.id " +
            "LEFT JOIN Ward w ON w.id=ai.ward.id " +
            "LEFT JOIN District d ON d.id = ai.district.id " +
            "LEFT JOIN City c ON c.id=ai.city.id " +
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=41")
    ParentVo findParentById (int id);
    //find all Parent List
    @Query("SELECT new fa.appcode.common.vo.ParentVo(ai.id,ai.fullName,ai.email,ai.phone,(CASE WHEN EXISTS (SELECT e FROM EnrollSchool e WHERE e.account.id = ai.id AND e.status != false) THEN true ELSE false END))" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.typeKey " +
            "WHERE ma.typeName='ROLE' AND ma.typeKey=3 AND (ai.fullName LIKE %?1% OR ai.email LIKE%?1% OR ai.phone LIKE %?1% ) AND ai.deleteFlg=false AND ai.statusId=41 " +
            "GROUP BY ai.id, ai.fullName, ai.email, ai.phone ")
    Page<ParentVo> findAllParent(String search, Pageable pageable);

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
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=41")
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId (int id,Pageable pageable);

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
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=41 AND s.account.email=?2 ")
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner (int parentId,String schoolOwnerId,Pageable pageable);

    //find account
    @Query("SELECT new fa.appcode.common.vo.RoleVo(m.typeValue) FROM AccountInfo ai JOIN MasterDatum m ON ai.roleId=m.typeKey AND m.typeName='ROLE' AND ai.email=?1")
    RoleVo findAccountVo(String email);

    AccountInfo getAccountInfoById(int id);
}
