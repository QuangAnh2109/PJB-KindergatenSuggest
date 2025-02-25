package fa.appcode.services;

import fa.appcode.common.vo.MasterDataVo;

import java.util.List;

public interface MasterDatumService {
    String findNameById(int id);
    List<MasterDataVo> findAllByTypeNameNoDelete(String typeName);
}