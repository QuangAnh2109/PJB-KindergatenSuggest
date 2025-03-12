package fa.appcode.repositories;

import fa.appcode.common.vo.EmailContentVo;
import fa.appcode.common.vo.MyRequestVo;
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

    /**
     * Find request details by request ID
     *
     * @param id the request ID
     * @return a RequestDetailVo containing request details
     */
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

    /**
     * List all requests, optionally filtered by account ID and request master ID
     *
     * @param accountID the account ID (nullable)
     * @param requestMasterID the request master ID (nullable)
     * @param pageable pagination information
     * @return a page of RequestVo
     */
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

    /**
     * Search for requests based on a keyword, optionally filtered by account ID and request master ID
     *
     * @param keyword the search keyword (nullable)
     * @param accountID the account ID (nullable)
     * @param requestMasterID the request master ID (nullable)
     * @param pageable pagination information
     * @return a page of RequestVo
     */
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

    /**
     * Update the status of a request
     *
     * @param update_id the ID of the user updating the request
     * @param id the request ID
     * @param updateTime the time of the update
     */
    @Modifying
    @Transactional
    @Query(""" 
            UPDATE Request r 
            SET r.requestMasterId = 2, r.updateId = ?1,r.updateTime= ?3
            WHERE r.id = ?2
            """)
    void updateRequestStatus(String update_id, int id, Instant updateTime);

    /**
     * Find accounts for sending email notifications
     *
     * @return a list of EmailContentVo containing account ID, email, and request count
     */
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


    @Query("""
    SELECT new fa.appcode.common.vo.MyRequestVo(
        r.id, r.fullName, r.requestEmail, r.requestPhone, 
        s.schoolName, s.schoolAddress, r.inquiries, m1.typeValue, 
        r.createTime, s.schoolEmail, 
        COALESCE(CAST(AVG((f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5) AS double), 0.0),
        COALESCE(CAST(COUNT(DISTINCT f.id) AS integer), 0), 
        CAST(s.feeFrom AS double), m2.typeValue, s.id
    )
    FROM Request r
    JOIN SchoolInfo s ON r.school.id = s.id
    JOIN AccountInfo a ON a.id = r.account.id
    JOIN MasterDatum m1 ON m1.typeKey = r.requestMasterId AND m1.typeName = 'REQUEST STATUS'
    JOIN MasterDatum m2 ON m2.typeKey = s.childReceivingAgeId AND m2.typeName = 'CHILD RECEIVING AGE'
    LEFT JOIN Feedback f ON f.school.id = s.id AND f.id.feedbackTime = (
        SELECT MAX(f2.id.feedbackTime) 
        FROM Feedback f2 
        WHERE f2.id.schoolId = s.id AND f2.id.accountId = f.id.accountId
        GROUP BY f2.id.accountId
    )
    WHERE r.account.id = :accountId AND r.deleteFlg = false
    GROUP BY  r.inquiries, m1.typeValue, 
             r.createTime, s.schoolEmail, s.feeFrom, m2.typeValue
""")
    Page<MyRequestVo> findRequestByAccountId(Integer accountId, Pageable pageable);





}
