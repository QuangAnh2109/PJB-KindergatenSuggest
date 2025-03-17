package fa.appcode.services.impl;

import fa.appcode.entities.Ward;
import fa.appcode.repositories.WardRepository;
import fa.appcode.services.WardService;
import fa.appcode.common.vo.WardVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;

    @Override
    public List<WardVo> findAllByDistrictIdAndNoDelete(int districtId) {
        return wardRepository.findAllByDistrictIdAndDeleteFlg(districtId, false);
    }

    @Override
    public WardVo findByIdAndNoDelete(int id) {
        return wardRepository.findByIdAndDeleteFlg(id, false);
    }

    @Override
    public Ward findByIdAndNoDeleteFlg(int id) {
        return wardRepository.findByIdAndDeleteFlag(id, false);
    }
}
