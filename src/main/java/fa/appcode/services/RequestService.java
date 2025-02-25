package fa.appcode.services;

import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestService {
    Page<RequestVo> findAll(Pageable pageable);
    Page<RequestVo> listAllRequestWithSchoolOwner(Integer accountID,Pageable pageable);


    RequestDetailVo findById(Integer id);

    Page<RequestVo> findOpenedRequest(Pageable pageable);
    Page<RequestVo> findOpenedRequestWithSchoolOwner(Integer accountID,Pageable pageable);

    Page<RequestVo> searchRequest(String keyword, Pageable pageable);
    Page<RequestVo> searchRequestWithSchoolOwner(String keyword,Integer accountID, Pageable pageable);

    Page<RequestVo> searchRequestReminder(String keyword, Pageable pageable);
    Page<RequestVo> searchRequestReminderWithSchoolOwner(String keyword,Integer accountID,Pageable pageable);

    void updateRequest(String update_id,int id);
}
