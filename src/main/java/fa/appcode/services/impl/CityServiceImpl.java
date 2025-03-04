package fa.appcode.services.impl;

import fa.appcode.repositories.CityRepository;
import fa.appcode.services.CityService;
import fa.appcode.common.vo.CityVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<CityVo> findAllByNoDelete() {
        return cityRepository.findAllByDeleteFlg(false);
    }

    @Override
    public CityVo findByIdAndNoDelete(int id) {
        return cityRepository.findByIdAndDeleteFlg(id, false);
    }
}
