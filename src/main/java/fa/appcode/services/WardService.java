package fa.appcode.services;

import fa.appcode.vo.WardVo;

import java.util.List;

public interface WardService {
    List<WardVo> findAllByDistrictIdAndDeleteFlg(Integer districtId, Boolean deleteFlg);
}
