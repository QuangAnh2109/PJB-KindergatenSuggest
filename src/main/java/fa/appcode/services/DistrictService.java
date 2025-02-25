package fa.appcode.services;

import fa.appcode.vo.DistrictVo;

import java.util.List;

public interface DistrictService {
    List<DistrictVo> findAllByCityIdAndDeleteFlg(Integer cityId, Boolean deleteFlg);
}
