package fa.appcode.services.impl;

import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import fa.appcode.repositories.RequestRepository;
import fa.appcode.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;


    @Override
    public Page<RequestVo> findAll(Pageable pageable) {
        return (Page<RequestVo>)requestRepository.listAllRequest(pageable);
    }

    @Override
    public RequestDetailVo findById(Integer id) {
        return requestRepository.findRequestsById(id);
    }

    @Override
    public Page<RequestVo> findOpenedRequest(Pageable pageable) {
        return (Page<RequestVo>)requestRepository.findOpenedRequest(pageable);
    }

    @Override
    public Page<RequestVo> searchRequest(String keyword, Pageable pageable) {
        return (Page<RequestVo>)requestRepository.searchRequest(keyword,pageable);
    }

    @Override
    public Page<RequestVo> searchRequestReminder(String keyword, Pageable pageable) {
        return (Page<RequestVo>)requestRepository.searchRequestReminder(keyword,pageable);
    }

    @Override
    public void updateRequest(String update_id,int id) {
        Instant vietnamTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        requestRepository.updateRequestStatus(update_id,id, vietnamTime);
    }
}
