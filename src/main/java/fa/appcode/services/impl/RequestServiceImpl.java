package fa.appcode.services.impl;

import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import fa.appcode.repositories.RequestRepository;
import fa.appcode.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;


    @Override
    public List<RequestVo> findAll() {
        return (List<RequestVo>)requestRepository.listAllRequest();
    }

    @Override
    public RequestDetailVo findById(Integer id) {
        return requestRepository.findRequestsById(id);
    }

    @Override
    public List<RequestVo> findOpenedRequest() {
        return (List<RequestVo>)requestRepository.findOpenedRequest();
    }
}
