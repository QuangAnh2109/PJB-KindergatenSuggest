package fa.appcode.services.impl;

import fa.appcode.repositories.MasterDataRepository;
import fa.appcode.services.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterDataServiceImpl implements MasterDataService {

    @Autowired
    private MasterDataRepository masterDataRepository;

    @Override
    public String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey) {
       return masterDataRepository.getMasterByTypeNameAndTypeKey(typeName, typeKey);
    }
}
