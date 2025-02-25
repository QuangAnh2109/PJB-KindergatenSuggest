package fa.appcode.services.impl;

import fa.appcode.common.vo.DistrictVo;
import fa.appcode.entities.District;
import fa.appcode.repositories.DistrictRepository;
import fa.appcode.services.DistrictService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;

    @Override
    public List<DistrictVo> getAllDistrictsByCityId(int cityId) {
        return districtRepository.getAllDistrictsByCityId(cityId);
    }
}
