package fa.appcode.services.impl;

import fa.appcode.repositories.DistrictRepository;
import fa.appcode.services.DistrictService;
import fa.appcode.vo.DistrictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<DistrictVo> findAllByCityIdAndNoDelete(Integer cityId) {
        return districtRepository.findAllByCityIdAndDeleteFlg(cityId, false);
    }
}
