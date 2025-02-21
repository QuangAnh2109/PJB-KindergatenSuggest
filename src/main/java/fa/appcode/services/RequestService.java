package fa.appcode.services;

import fa.appcode.entities.Request;

import java.util.List;

public interface RequestService {
    List<Request> findAll();

    Request findById(Integer id);
    List<Request> findOpenedRequest();
}
