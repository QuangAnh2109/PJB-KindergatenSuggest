package fa.appcode.services;

import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;

import java.util.List;

public interface RequestService {
    List<RequestVo> findAll();

    RequestDetailVo findById(Integer id);
    List<RequestVo> findOpenedRequest();
}
