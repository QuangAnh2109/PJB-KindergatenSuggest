package fa.appcode.repositories;

import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query("Select new fa.appcode.common.vo.RequestDetailVo(r.id,r.fullName,r.requestEmail,r.requestPhone,s.schoolAddress,s.schoolName,r.inquiries,m.typeValue)"
            + "From MasterDatum m Join Request r ON m.id = r.requestMasterId "
            + "Join SchoolInfo s On r.school.id=s.id "
            + "where r.id = ?1")
    RequestDetailVo findRequestsById(Integer id);

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From MasterDatum m Join Request r ON m.id = r.requestMasterId "
            + "where r.requestMasterId !=44")
    List<RequestVo> findOpenedRequest();

    @Query("Select new fa.appcode.common.vo.RequestVo(r.id,r.fullName,r.requestEmail,r.requestPhone,m.typeValue)"
            + "From MasterDatum m Join Request r ON m.id = r.requestMasterId")
    List<RequestVo> listAllRequest();
}
