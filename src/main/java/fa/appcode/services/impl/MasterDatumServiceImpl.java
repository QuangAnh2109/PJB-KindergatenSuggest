package fa.appcode.services.impl;

import fa.appcode.entities.MasterDatum;
import fa.appcode.repositories.MasterDatumRepository;
import fa.appcode.services.MasterDatumService;
import fa.appcode.vo.MasterDataVo;
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
}
