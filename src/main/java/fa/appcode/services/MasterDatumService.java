package fa.appcode.services;

import fa.appcode.entities.MasterDatum;
import fa.appcode.vo.MasterDataVo;

import java.util.List;

public interface MasterDatumService {
    String findNameById(int id);
    List<MasterDataVo> findAllByTypeNameNoDelete(String typeName);
}