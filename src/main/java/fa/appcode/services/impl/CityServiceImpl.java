package fa.appcode.services.impl;
import fa.appcode.common.vo.CityVo;
import fa.appcode.repositories.CityRepository;
import fa.appcode.services.CityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<CityVo> getAllCities() {
        return cityRepository.getAllCities();
    }

}
