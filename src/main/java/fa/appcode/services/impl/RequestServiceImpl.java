package fa.appcode.services.impl;

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
    public List<Request> findAll() {
        return (List<Request>)requestRepository.findAll();
    }

    @Override
    public Request findById(Integer id) {
        return requestRepository.findRequestsById(id);
    }

    @Override
    public List<Request> findOpenedRequest() {
        return (List<Request>)requestRepository.findOpenedRequest();
    }
}
