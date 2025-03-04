package fa.appcode.services;

import fa.appcode.common.vo.WardVo;

import java.util.List;

public interface WardService {
    List<WardVo> findAllByDistrictIdAndNoDelete(int districtId);

    WardVo findByIdAndNoDelete(int id);
}
