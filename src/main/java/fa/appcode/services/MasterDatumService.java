package fa.appcode.services;

import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.entities.MasterDatum;

import java.util.List;

public interface MasterDatumService {
    String findNameById(int id);
    List<MasterDataVo> findAllByTypeNameNoDelete(String typeName);


    /**
     * Get the master value based on the provided type name and type key.
     *
     * @param typeName
     * @param typeKey
     * @return
     */
     String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey);

    /**
     * Get a list of master data based on the provided type name.
     *
     * @param typeName
     * @return
     */
     List<MasterDatum> getListByTypeName(String typeName);

    /**
     * Get the master key based on the provided type name and type value.
     *
     * @param typeName
     * @param typeValue
     * @return
     */
     Integer getMasterKeyByTypeNameAndTypeValue(String typeName, String typeValue);
}