package fa.appcode.services.impl;

import fa.appcode.repositories.MasterDatumRepository;
import fa.appcode.services.MasterDatumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterDatumServiceImpl implements MasterDatumService {
   @Autowired
   private MasterDatumRepository masterDatumRepository;
    @Override
    public String findNameById(int id) {
        return masterDatumRepository.getById(id).getTypeValue();
    }
}
