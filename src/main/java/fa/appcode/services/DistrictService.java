package fa.appcode.services;

import fa.appcode.common.vo.DistrictVo;

import java.util.List;

public interface DistrictService {
    List<DistrictVo> findAllByCityIdAndNoDelete(int cityId);

    DistrictVo findByIdAndNoDelete(int id);
}
