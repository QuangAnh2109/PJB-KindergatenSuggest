package fa.appcode.services;

import fa.appcode.common.vo.CityVo;

import java.util.List;

public interface CityService {
    List<CityVo> findAllByNoDelete();

    CityVo findByIdAndNoDelete(int id);
}
