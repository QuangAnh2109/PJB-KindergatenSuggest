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
              FROM  Request r 
              JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS" 
              WHERE r.requestMasterId !=2
            """)
    Page<RequestVo> findOpenedRequest(Pageable pageable);

    @Query("""
            SELECT new fa.appcode.common.vo.RequestVo(
                r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue) 
            FROM Request r 
            JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS"
            JOIN SchoolInfo s On r.school.id=s.id 
            JOIN AccountInfo a On a.id=s.account.id 
            WHERE r.requestMasterId !=2 And a.id = ?1 
            """)
    Page<RequestVo> findOpenedRequestWithSchoolOwner(Integer accountID,Pageable pageable);

    @Query(""" 
           SELECT new fa.appcode.common.vo.RequestVo(
                      r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)
           FROM Request r 
           JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS" 
           """)
    Page<RequestVo> listAllRequest(Pageable pageable);

    @Query("""
            SELECT new fa.appcode.common.vo.RequestVo(
                        r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)
            FROM Request r 
            JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS"
            JOIN SchoolInfo s ON r.school.id=s.id 
            JOIN AccountInfo a ON a.id=s.account.id 
            WHERE a.id = ?1 
            """)
    Page<RequestVo>  listAllRequestWithSchoolOwner(Integer accountID,Pageable pageable);

    @Query("""
          SELECT new fa.appcode.common.vo.RequestVo(
                    r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)
          FROM  Request r 
          JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS" 
          WHERE r.fullName LIKE %?1% 
             OR r.requestEmail LIKE %?1% 
             OR r.requestPhone LIKE %?1% 
             OR m.typeValue LIKE %?1% 
          """)
    Page<RequestVo> searchRequest(String keyword,Pageable pageable);

    @Query("""
            SELECT new fa.appcode.common.vo.RequestVo(
                        r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)
            FROM  Request r 
            JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS"
            JOIN SchoolInfo s ON r.school.id=s.id 
            JOIN AccountInfo a ON a.id=s.account.id 
            where (r.fullName LIKE %?1% 
                OR r.requestEmail LIKE %?1% 
                OR r.requestPhone LIKE %?1% 
                OR m.typeValue LIKE %?1%) 
                AND a.id = ?2 
            """)
    Page<RequestVo> searchRequestWithSchoolOwner(String keyword,Integer accountID,Pageable pageable);

    @Query("""
            SELECT new fa.appcode.common.vo.RequestVo(
                       r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)
            FROM  Request r 
            JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS" 
            JOIN SchoolInfo s ON r.school.id=s.id 
            JOIN AccountInfo a ON a.id=s.account.id 
            where (r.fullName LIKE %?1% 
                OR r.requestEmail LIKE %?1% 
                Or r.requestPhone LIKE %?1% 
                OR m.typeValue LIKE %?1%) 
                AND a.id = ?2 
                AND r.requestMasterId!=2 
           """)
    Page<RequestVo> searchRequestReminderWithSchoolOwner(String keyword,Integer accountID,Pageable pageable);

    @Query(""" 
            SELECT new fa.appcode.common.vo.RequestVo(
                        r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)
            FROM  Request r 
            JOIN MasterDatum m ON m.typeKey = r.requestMasterId AND m.typeName="REQUEST STATUS"
            WHERE (r.fullName LIKE %?1% 
                OR r.requestEmail LIKE %?1% 
                OR r.requestPhone LIKE %?1% 
                OR m.typeValue LIKE %?1%) 
                AND r.requestMasterId !=2 
            """)
    Page<RequestVo> searchRequestReminder(String keyword,Pageable pageable);


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
