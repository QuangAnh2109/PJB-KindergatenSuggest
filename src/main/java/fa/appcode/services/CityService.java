package fa.appcode.services;

import fa.appcode.vo.CityVo;

import java.util.List;

public interface CityService {
    List<CityVo> findAllByDeleteFlg(Boolean deleteFlg);
}
