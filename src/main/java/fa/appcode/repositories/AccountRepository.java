package fa.appcode.repositories;
import com.cloudinary.provisioning.Account;
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
    @Query("SELECT c FROM AccountInfo c WHERE c.email = ?1")
    AccountVo findAccountByEmail(String email);
    @Query("SELECT c FROM AccountInfo c WHERE c.phone = ?1")
    AccountInfo findAccountByPhone(String email);
    @Modifying
    @Transactional
    @Query("UPDATE AccountInfo a SET a.password = ?1 WHERE a.email = ?2")
    int updatePassword(String newPassword, String email);

    @Query("""
                SELECT new fa.appcode.common.vo.AccountVo(
                    ai.id, ai.fullName, ai.email, ai.phone, ai.dob, 
                    CONCAT(ai.address, ', ', w.wardName, ', ', d.districtName, ', ', c.cityName), 
                    ma.typeValue, ms.typeValue)
                FROM AccountInfo ai
                LEFT JOIN ai.ward w
                LEFT JOIN ai.district d
                LEFT JOIN ai.city c     
                LEFT JOIN MasterDatum ma ON ai.roleId = ma.id
                LEFT JOIN MasterDatum ms ON ai.statusId = ms.id
                WHERE ai.deleteFlg = false
                AND (:search IS NULL OR ai.fullName LIKE %:search% OR ai.email LIKE %:search% OR ai.phone LIKE %:search%)
                """)
    Page<AccountVo> findAllWithFullAddress(@Param("search") String search, Pageable pageable);

    /**
     * @param pageable
     * @return
     */

    @Query("select m from AccountInfo m join MasterDatum ma ON m.roleId=ma.typeKey WHERE ma.typeName='ROLE' and ma.typeKey=3")
    Page<AccountInfo> findAll(Pageable pageable);

    @Query("select m from AccountInfo m join MasterDatum ma ON m.roleId=ma.typeKey WHERE ma.typeName='ROLE' and ma.typeKey=3")
    List<AccountInfo> findAllRole();

    // Find Parent data by parent Id
    @Query("SELECT new fa.appcode.common.vo.ParentVo(ai.id,ai.fullName,ai.email,ai.phone,ai.dob,TRIM(BOTH ' ' FROM CONCAT(COALESCE(ai.address, ''), '   ', COALESCE(w.wardName, ''), '   ', COALESCE(d.districtName, ''), '   ', COALESCE(c.cityName, ''))))" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.id " +
            "LEFT JOIN Ward w ON w.id=ai.ward.id " +
            "LEFT JOIN District d ON d.id = ai.district.id " +
            "LEFT JOIN City c ON c.id=ai.city.id " +
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=41")
    ParentVo findParentById (int id);
//    find all Parent List
    @Query("SELECT new fa.appcode.common.vo.ParentVo(ai.id,ai.fullName,ai.email,ai.phone, false)" +
//            "(CASE WHEN EXISTS (SELECT e FROM EnrollSchool e WHERE e.account.id = ai.id AND e.status != false) THEN true ELSE false END))" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.typeKey " +
            "WHERE ma.id=3 AND (ai.fullName LIKE %?1% OR ai.email LIKE%?1% OR ai.phone LIKE %?1% ) AND ai.deleteFlg=false AND ai.statusId=41 " +
            "GROUP BY ai.id, ai.fullName, ai.email, ai.phone ")
    Page<ParentVo> findAllParent(String search, Pageable pageable);

    @Query("SELECT new fa.appcode.common.vo.ParentVo(m.id,m.fullName,m.email,m.phone,false) " +
//            "(CASE WHEN EXISTS (SELECT e FROM EnrollSchool e WHERE e.account.id = m.id AND e.status != false) THEN true ELSE false END))" +
            "FROM AccountInfo m " +
            "JOIN MasterDatum ma ON m.roleId=ma.typeKey " +
            "LEFT JOIN  EnrollSchool e ON e.account.id=m.id " +
            "LEFT JOIN SchoolInfo s ON s.id=e.school.id " +
            "WHERE ma.id=3 " +
            "GROUP BY m.id, m.fullName, m.email, m.phone")
    Page<ParentVo> findAllParent(Pageable pageable);

    //find account role by email
    @Query("SELECT m.typeValue FROM AccountInfo ai JOIN MasterDatum m ON ai.roleId=m.id AND ai.email=?1")
    String findAccountRoleString(String email);

    AccountInfo getAccountInfoById(int id);
    //find all parent for School Owner List
    @Query("SELECT new fa.appcode.common.vo.ParentVo(ai.id,ai.fullName,ai.email,ai.phone, false) " +
//            "(CASE WHEN EXISTS (SELECT e FROM EnrollSchool e WHERE e.account.id = ai.id AND e.status != false AND e.school.account.email=:email) THEN true ELSE false END))" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.typeKey " +
            "WHERE ma.id=3 AND (ai.fullName LIKE %:search% OR ai.email LIKE%:search% OR ai.phone LIKE %:search% ) AND ai.deleteFlg=false AND ai.statusId=41 " +
            "GROUP BY ai.id, ai.fullName, ai.email, ai.phone ")
    Page<ParentVo> findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(@Param("email") String email,@Param("search") String search, Pageable pageable);

}
