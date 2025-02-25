package fa.appcode.services.impl;

import fa.appcode.entities.MasterDatum;
import fa.appcode.repositories.MasterDataRepository;
import fa.appcode.services.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterDataServiceImpl implements MasterDataService {

    @Autowired
    private MasterDataRepository masterDataRepository;

//    @Override
//    public String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey) {
//       return masterDataRepository.getMasterByTypeNameAndTypeKey(typeName, typeKey);
//    }

    @Override
    public String getMasterById(Integer id) {
        return masterDataRepository.getMasterById(id);
    }

    @Override
    public List<MasterDatum> getListByTypeName(String typeName) {
        return masterDataRepository.getMasterByTypeName(typeName);
    }
}
