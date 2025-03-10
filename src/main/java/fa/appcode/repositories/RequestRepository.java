package fa.appcode.repositories;

import fa.appcode.common.vo.EmailContentVo;
import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query("""
            SELECT new fa.appcode.common.vo.RequestDetailVo(
                        r.id,r.fullName,r.requestEmail,r.requestPhone,
                        s.schoolAddress,s.schoolName,r.inquiries,m.typeValue)
            FROM  Request r
            JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS"
            JOIN SchoolInfo s ON r.school.id=s.id 
            WHERE r.id = ?1
            """)
    RequestDetailVo findRequestsById(Integer id);

    @Query(""" 
            SELECT new fa.appcode.common.vo.RequestVo(
                       r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)
            FROM Request r 
            JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS" 
            JOIN SchoolInfo s ON r.school.id=s.id 
            JOIN AccountInfo a ON a.id=s.account.id
            WHERE (:accountID IS NULL OR a.id = :accountID)
            AND (:requestMasterID IS NULL OR r.requestMasterId != 2)
            """)
    Page<RequestVo> listAllRequest(@Param("accountID") Integer accountID,@Param("requestMasterID") Integer requestMasterID, Pageable pageable);

    @Query("""
                SELECT new fa.appcode.common.vo.RequestVo(
                           r.id, r.fullName, r.requestEmail, r.requestPhone, m.typeValue)
                FROM Request r
                JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName = 'REQUEST STATUS'
                JOIN SchoolInfo s ON r.school.id = s.id
                JOIN AccountInfo a ON a.id = s.account.id
                WHERE (:keyword IS NULL OR :keyword = '' OR 
                       r.fullName LIKE CONCAT('%', :keyword, '%') OR
                       r.requestEmail LIKE CONCAT('%', :keyword, '%') OR
                       r.requestPhone LIKE CONCAT('%', :keyword, '%') OR
                       m.typeValue LIKE CONCAT('%', :keyword, '%'))
                      AND (:accountID IS NULL OR a.id = :accountID)
                      AND (:requestMasterID IS NULL OR r.requestMasterId != 2)
                      """)
    Page<RequestVo> searchRequest(@Param("keyword")  String keyword, @Param("accountID")  Integer accountID, @Param("requestMasterID")  Integer requestMasterID, Pageable pageable);

    @Modifying
    @Transactional
    @Query(""" 
            UPDATE Request r 
            SET r.requestMasterId = 2, r.updateId = ?1,r.updateTime= ?3
            WHERE r.id = ?2
            """)
    void updateRequestStatus(String update_id, int id, Instant updateTime);

    @Query(""" 
            SELECT  new fa.appcode.common.vo.EmailContentVo(
                        a.id,a.email,count(a.id))
            FROM  Request r 
            JOIN SchoolInfo s On r.school.id=s.id 
            JOIN AccountInfo a On a.id=s.account.id 
            WHERE r.requestMasterId!=2
            GROUP BY a.id,a.email 
            """)
    List<EmailContentVo> findAccountForEmail();

    //Find all request of parent by acccountId
    @Query("""
        SELECT new fa.appcode.common.vo.RequestDetailVo(
                        r.id,r.fullName,r.requestEmail,r.requestPhone,
                        s.schoolAddress,s.schoolName,r.inquiries,m.typeValue)
            FROM  Request r
            JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS"
            JOIN SchoolInfo s ON r.school.id=s.id
            JOIN AccountInfo a ON a.id=s.account.id 
            WHERE r.account.id=?1 AND r.deleteFlg = false
    """)
    Page<RequestDetailVo> findRequestByAccountId(Integer accountId,Pageable pageable);

//    @Query("""
//                SELECT new fa.appcode.common.vo.MyRequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,s.schoolName,s.schoolAddress,r.inquiries,m.typeValue,r.createTime,s.schoolEmail)
//                From Request r
//                JOIN SchoolInfo s ON r.school.id=s.id
//                JOIN AccountInfo a ON a.id=r.account.id
//                JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS"
//                WHERE r.account.id=?1 AND r.deleteFlg = false
//            """)
//    Page<RequestDetailVo> findRequestByAccountId1(Integer accountId,Pageable pageable);


}
