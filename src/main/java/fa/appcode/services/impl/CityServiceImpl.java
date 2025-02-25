package fa.appcode.services.impl;

import fa.appcode.repositories.CityRepository;
import fa.appcode.services.CityService;
import fa.appcode.common.vo.CityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<CityVo> findAllByNoDelete(){
        return cityRepository.findAllByDeleteFlg(false);
    }
}
