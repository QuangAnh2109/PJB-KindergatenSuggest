package fa.appcode.services.impl;

import fa.appcode.entities.MasterDatum;
import fa.appcode.repositories.MasterDatumRepository;
import fa.appcode.services.MasterDatumService;
import fa.appcode.common.vo.MasterDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterDatumServiceImpl implements MasterDatumService {
    @Autowired
    private MasterDatumRepository masterDatumRepository;

    @Override
    public String findNameById(int id) {
        return masterDatumRepository.getById(id).getTypeValue();
    }

    @Override
    public List<MasterDataVo> findAllByTypeNameNoDelete(String typeName) {
        return masterDatumRepository.findAllMasterDataVoByTypeNameAndDeleteFlg(typeName, false);
    }

    @Override
    public List<MasterDataVo> findAllByTypeNameInNoDelete(List<String> typeName) {
        return masterDatumRepository.findAllMasterDataVoByTypeNameInAndDeleteFlg(typeName, false);
    }

  @Override
    public String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey) {
       return masterDatumRepository.getMasterByTypeNameAndTypeKey(typeName, typeKey);
    }

    @Override
    public List<MasterDatum> getListByTypeName(String typeName) {
        return masterDatumRepository.getMasterByTypeName(typeName);
    }

    @Override
    public Integer getMasterKeyByTypeNameAndTypeValue(String typeName, String typeValue) {
        return masterDatumRepository.getMasterKeyByTypeNameAndTypeValue(typeName,typeValue);
    }
}
