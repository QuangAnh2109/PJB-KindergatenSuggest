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
}
