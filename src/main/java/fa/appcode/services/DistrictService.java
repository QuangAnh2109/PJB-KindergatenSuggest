package fa.appcode.services;

import fa.appcode.common.vo.DistrictVo;
import fa.appcode.entities.District;

import java.util.List;

public interface DistrictService {
    List<DistrictVo> findAllByCityIdAndNoDelete(int cityId);

    DistrictVo findByIdAndNoDelete(int id);

    District findByIdAndNoDeleteFlag(int id);
}
