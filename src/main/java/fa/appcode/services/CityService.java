package fa.appcode.services;

import fa.appcode.common.vo.CityVo;
import fa.appcode.entities.City;

import java.util.List;

public interface CityService {
    List<CityVo> findAllByNoDelete();

    CityVo findByIdAndNoDelete(int id);

    City findByIdAndNoDeleteFlg(int id);
}
