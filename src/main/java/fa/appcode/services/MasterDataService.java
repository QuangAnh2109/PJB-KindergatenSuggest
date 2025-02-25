package fa.appcode.services;

import fa.appcode.entities.MasterDatum;

import java.util.List;

public interface MasterDataService {
   // String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey);
    String getMasterById(Integer id);
    List<MasterDatum> getListByTypeName(String type);

}
