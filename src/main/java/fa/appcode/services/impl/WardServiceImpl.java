package fa.appcode.services.impl;

import fa.appcode.repositories.WardRepository;
import fa.appcode.services.WardService;
import fa.appcode.vo.WardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardServiceImpl implements WardService {
    @Autowired
    private WardRepository wardRepository;

    @Override
    public List<WardVo> findAllByDistrictIdAndDeleteFlg(Integer districtId, Boolean deleteFlg) {
        return wardRepository.findAllByDistrictIdAndDeleteFlg(districtId, deleteFlg);
    }
}
