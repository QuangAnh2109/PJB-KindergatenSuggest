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

    @Query("Select new fa.appcode.common.vo.RequestDetailVo(r.id,r.fullName,r.requestEmail,r.requestPhone,s.schoolAddress,s.schoolName,r.inquiries,m.typeValue)"
            + "From MasterDatum m Join Request r ON m.id = r.requestMasterId "
            + "Join SchoolInfo s On r.school.id=s.id "
            + "where r.id = ?1")
    RequestDetailVo findRequestsById(Integer id);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From  Request r Join MasterDatum m ON m.id = r.requestMasterId "
            + "where r.requestMasterId !=44")
    Page<RequestVo> findOpenedRequest(Pageable pageable);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue) "
            + "From Request r Join MasterDatum m ON m.id = r.requestMasterId "
            + "Join SchoolInfo s On r.school.id=s.id "
            + "Join AccountInfo a On a.id=s.account.id "
            + "where r.requestMasterId !=44 And a.id = ?1")
    Page<RequestVo> findOpenedRequestWithSchoolOwner(Integer accountID,Pageable pageable);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From Request r Join MasterDatum m  ON m.id = r.requestMasterId")
    Page<RequestVo> listAllRequest(Pageable pageable);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From Request r Join MasterDatum m ON m.id = r.requestMasterId "
            + "Join SchoolInfo s On r.school.id=s.id "
            + "Join AccountInfo a On a.id=s.account.id "
            + "where a.id = ?1")
    Page<RequestVo>  listAllRequestWithSchoolOwner(Integer accountID,Pageable pageable);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From  Request r Join MasterDatum m ON m.id = r.requestMasterId "
            + "where r.fullName Like %?1% Or r.requestEmail Like %?1% "
            + "Or r.requestPhone Like %?1% Or m.typeValue Like %?1%")
    Page<RequestVo> searchRequest(String keyword,Pageable pageable);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From  Request r Join MasterDatum m ON m.id = r.requestMasterId "
            + "Join SchoolInfo s On r.school.id=s.id "
            + "Join AccountInfo a On a.id=s.account.id "
            + "where (r.fullName Like %?1% Or r.requestEmail Like %?1% "
            + "Or r.requestPhone Like %?1% Or m.typeValue Like %?1%) And a.id = ?2")
    Page<RequestVo> searchRequestWithSchoolOwner(String keyword,Integer accountID,Pageable pageable);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From  Request r Join MasterDatum m ON m.id = r.requestMasterId "
            + "Join SchoolInfo s On r.school.id=s.id "
            + "Join AccountInfo a On a.id=s.account.id "
            + "where (r.fullName Like %?1% Or r.requestEmail Like %?1% "
            + "Or r.requestPhone Like %?1% Or m.typeValue Like %?1%) And a.id = ?2 And r.requestMasterId!=44")
    Page<RequestVo> searchRequestReminderWithSchoolOwner(String keyword,Integer accountID,Pageable pageable);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From  Request r Join MasterDatum m ON m.id = r.requestMasterId "
            + "where (r.fullName Like %?1% Or r.requestEmail Like %?1% "
            + "Or r.requestPhone Like %?1% Or m.typeValue Like %?1%) "
            + "And r.requestMasterId !=44")
    Page<RequestVo> searchRequestReminder(String keyword,Pageable pageable);


    @Modifying
    @Transactional
    @Query("Update Request r Set r.requestMasterId = 44, r.updateId = ?1,r.updateTime= ?3"+
           " Where r.id = ?2")
    void updateRequestStatus(String update_id, int id, Instant updateTime);

    @Query("Select  new fa.appcode.common.vo.EmailContentVo(a.id,a.email,count(a.id))"
            + "From  Request r "
            + "Join SchoolInfo s On r.school.id=s.id "
            + "Join AccountInfo a On a.id=s.account.id "
            + "Where r.requestMasterId!=44"
            + "Group By a.id,a.email")
    List<EmailContentVo> findAccountForEmail();
}
