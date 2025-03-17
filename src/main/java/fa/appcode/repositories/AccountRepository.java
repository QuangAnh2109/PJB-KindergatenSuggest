package fa.appcode.repositories;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.entities.AccountInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("accountRepository")
@Transactional
public interface AccountRepository extends JpaRepository<AccountInfo, Integer> {

    @Query("SELECT c FROM AccountInfo c WHERE c.email = ?1 AND c.deleteFlg = false")
    AccountInfo findByEmail(String email);

    @Query("SELECT c FROM AccountInfo c WHERE c.phone = ?1 AND c.deleteFlg = false")
    AccountInfo findAccountByPhone(String phone);

    @Query("Select c.recordNo from  AccountInfo c where c.email=?1 and c.deleteFlg=false")
    AccountVo getRecordByEmail(String email);

    /**
     * get a list of user accounts along with their full addresses by search criteria by name, email,phone .
     * <p>
     * The address information includes:
     * - Account address
     * - Ward name
     * - District name
     * - City name.
     *
     * @param search
     * @param pageable
     * @return
     */
    @Query("""
            SELECT new fa.appcode.common.vo.AccountVo(
                ai.id, ai.fullName, ai.email, ai.phone, ai.dob, 
                CONCAT(ai.address, ', ', w.wardName, ', ', d.districtName, ', ', c.cityName), 
                ma.typeValue, ms.typeValue)
            FROM AccountInfo ai
            LEFT JOIN ai.ward w
            LEFT JOIN ai.district d
            LEFT JOIN ai.city c     
            LEFT JOIN MasterDatum ma ON ai.roleId = ma.typeKey AND ma.typeName = "ROLE"
            LEFT JOIN MasterDatum ms ON ai.statusId = ms.typeKey AND ms.typeName="ACCOUNT STATUS"
            WHERE ai.deleteFlg = false
            AND (:search IS NULL OR ai.fullName LIKE %:search% OR ai.email LIKE %:search% OR ai.phone LIKE %:search%)
            """)
    Page<AccountVo> findAllWithFullAddress(@Param("search") String search, Pageable pageable);

    // Find Parent data by parent Id
    @Query("SELECT new fa.appcode.common.vo.ParentVo(ai.id,ai.fullName,ai.email,ai.phone,ai.dob,TRIM(BOTH ' ' FROM CONCAT(COALESCE(ai.address, ''), '   ', COALESCE(w.wardName, ''), '   ', COALESCE(d.districtName, ''), '   ', COALESCE(c.cityName, ''))))" +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.id AND ma.typeName='ROLE'" +
            "LEFT JOIN Ward w ON w.id=ai.ward.id " +
            "LEFT JOIN District d ON d.id = ai.district.id " +
            "LEFT JOIN City c ON c.id=ai.city.id " +
            "WHERE ai.id=?1 AND ai.deleteFlg=false AND ai.statusId=1")
    ParentVo findParentById(int id);

    //    find all Parent List
    @Query("SELECT new fa.appcode.common.vo.ParentVo(ai.id,ai.fullName,ai.email,ai.phone, " +
            "CASE WHEN EXISTS (SELECT 1 FROM EnrollSchool e WHERE e.account.id = ai.id AND e.status = 1) THEN " +
            "(SELECT md.typeValue FROM MasterDatum md WHERE md.typeKey = 1 AND md.typeName='ENROLL STATUS') " +
            "WHEN EXISTS (SELECT 1 FROM EnrollSchool e WHERE e.account.id = ai.id AND e.status = 3) " +
            "AND NOT EXISTS (SELECT 1 FROM EnrollSchool e WHERE e.account.id = ai.id AND e.status = 1) " +
            "THEN (SELECT md.typeValue FROM MasterDatum md WHERE md.typeKey = 3 AND md.typeName='ENROLL STATUS')" +
            "ELSE 'Not Enroll' END ) " +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.typeKey AND ma.typeName='ROLE' " +
            "LEFT JOIN EnrollSchool e ON ai.id = e.account.id " +
            "LEFT JOIN SchoolInfo s ON e.school.id = s.id " +
            "WHERE ma.id=3 AND (ai.fullName LIKE %?1% OR ai.email LIKE%?1% OR ai.phone LIKE %?1% ) AND ai.deleteFlg=false AND ai.statusId=1 " +
            "GROUP BY ai.id, ai.fullName, ai.email, ai.phone ")
    Page<ParentVo> findAllParent(String search, Pageable pageable);


    //find account role by email
    @Query("SELECT m.typeValue FROM AccountInfo ai JOIN MasterDatum m ON ai.roleId=m.id AND ai.email=?1")
    String findAccountRoleString(String email);

    AccountInfo getAccountInfoById(int id);


    //find all parent for School Owner List
    @Query("SELECT new fa.appcode.common.vo.ParentVo(ai.id,ai.fullName,ai.email,ai.phone, " +
            "CASE WHEN EXISTS (SELECT 1 FROM EnrollSchool e JOIN SchoolInfo si ON e.school.id=si.id WHERE e.account.id = ai.id AND e.status = 1 AND si.account.email = :email) " +
            "THEN (SELECT md.typeValue FROM MasterDatum md WHERE md.typeKey = 1 AND md.typeName='ENROLL STATUS') " +
            "WHEN EXISTS (SELECT 1 FROM EnrollSchool e JOIN SchoolInfo si ON e.school.id=si.id WHERE e.account.id = ai.id AND e.status = 3 AND si.account.email = :email) " +
            "AND NOT EXISTS (SELECT 1 FROM EnrollSchool e JOIN SchoolInfo si ON e.school.id=si.id WHERE e.account.id = ai.id AND e.status = 1 AND si.account.email = :email) " +
            "THEN (SELECT md.typeValue FROM MasterDatum md WHERE md.typeKey = 3 AND md.typeName='ENROLL STATUS')" +
            "ELSE 'Not Enroll' END ) " +
            "FROM AccountInfo ai " +
            "JOIN MasterDatum ma ON ai.roleId=ma.typeKey AND ma.typeName='ROLE' " +
            "LEFT JOIN EnrollSchool e ON ai.id = e.account.id " +
            "LEFT JOIN SchoolInfo s ON e.school.id = s.id AND s.account.email = :email " +
            "WHERE ma.id=3 AND (ai.fullName LIKE %:search% OR ai.email LIKE%:search% OR ai.phone LIKE %:search% ) AND ai.deleteFlg=false AND ai.statusId=1 " +
            "GROUP BY ai.id,ai.fullName,ai.email,ai.phone")
    Page<ParentVo> findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(@Param("email") String email, @Param("search") String search, Pageable pageable);

    @Query("SELECT ai.email FROM AccountInfo ai WHERE ai.id = :id AND ai.statusId = :statusId AND ai.deleteFlg = :deleteFlg")
    String getEmailByAccountIdAndStatusIdAndDeleteFlg(@Param("id") int id, @Param("statusId") int statusId, @Param("deleteFlg") boolean deleteFlg);

    @Query("SELECT a FROM AccountInfo a " +
            "JOIN FETCH a.city " +
            "JOIN FETCH a.district " +
            "JOIN FETCH a.ward " +
            "WHERE a.email = :email AND a.deleteFlg = :deleteFlg")
    AccountInfo findWithFullAddressByEmail(@Param("email") String email, @Param("deleteFlg") boolean deleteFlg);

    @Query("SELECT si.account.email FROM SchoolInfo si WHERE si.id = :id AND si.account.statusId = :statusId AND si.account.deleteFlg = :deleteFlg")
    String getSchoolOwnerEmailBySchoolIdAndStatusAndDeleteFlg(@Param("id") int id, @Param("statusId") int statusId, @Param("deleteFlg") boolean deleteFlg);

    AccountInfo findAccountByEmailAndStatusIdAndDeleteFlg(String email, int statusId, boolean deleteFlg);

    @Query("SELECT a.fullName FROM AccountInfo a WHERE a.email = ?1 AND a.deleteFlg = ?2")
    String getAccountNameByEmailAndDeleteFlg(String email, boolean deleteFlg);
}
