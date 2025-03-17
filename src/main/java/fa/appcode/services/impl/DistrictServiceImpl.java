package fa.appcode.services.impl;

import fa.appcode.entities.District;
import fa.appcode.repositories.DistrictRepository;
import fa.appcode.services.DistrictService;
import fa.appcode.common.vo.DistrictVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Override
    public List<DistrictVo> findAllByCityIdAndNoDelete(int cityId) {
        return districtRepository.findAllByCityIdAndDeleteFlg(cityId, false);
    }

    @Override
    public DistrictVo findByIdAndNoDelete(int id) {
        return districtRepository.findByIdAndDeleteFlg(id, false);
    }

    @Override
    public District findByIdAndNoDeleteFlag(int id) {
        return districtRepository.findByIdAndDeleteFalg(id, false);
    }
}
