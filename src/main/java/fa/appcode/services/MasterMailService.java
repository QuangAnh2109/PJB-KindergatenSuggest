package fa.appcode.services;

import fa.appcode.common.vo.MasterMailVo;

public interface MasterMailService {
    MasterMailVo findByIdAndNoDelete(Integer id);
}
