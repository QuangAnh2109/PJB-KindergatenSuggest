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
        return masterDatumRepository.findAllByTypeNameAndDeleteFlg(typeName, false);
    }


//    @Override
//    public String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey) {
//       return masterDataRepository.getMasterByTypeNameAndTypeKey(typeName, typeKey);
//    }

    @Override
    public String getMasterById(Integer id) {
        return masterDatumRepository.getMasterById(id);
    }

    @Override
    public List<MasterDatum> getListByTypeName(String typeName) {
        return masterDatumRepository.getMasterByTypeName(typeName);
    }
}
