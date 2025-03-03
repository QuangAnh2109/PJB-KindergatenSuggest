package fa.appcode.services;

import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.entities.MasterDatum;

import java.util.List;

public interface MasterDatumService {
    String findNameById(int id);

    List<MasterDataVo> findAllByTypeNameNoDelete(String typeName);

    List<MasterDataVo> findAllByTypeNameInNoDelete(List<String> typeName);

    // String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey);
    String getMasterById(Integer id);

    List<MasterDatum> getListByTypeName(String type);

}