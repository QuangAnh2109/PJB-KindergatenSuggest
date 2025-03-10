package fa.appcode.services;

import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RequestService {
    Page<RequestVo> listAllRequest(Integer accountID,Integer requestMasterID,Pageable pageable);

    RequestDetailVo findById(Integer id);

    Page<RequestVo> searchRequest(String keyword, Integer accountID, Integer requestMasterID, Pageable pageable);

    Page<RequestDetailVo> findRequestByAccountId(Integer accountId,Pageable pageable);

    void updateRequest(String update_id, int id);

    void emailRequestReminder();

    void createRequest(Request request);
}
