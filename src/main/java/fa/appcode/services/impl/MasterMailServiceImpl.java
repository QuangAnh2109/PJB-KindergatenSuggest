package fa.appcode.services.impl;

import fa.appcode.common.vo.MasterMailVo;
import fa.appcode.repositories.MasterMailRepository;
import fa.appcode.services.MasterMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterMailServiceImpl implements MasterMailService {
    @Autowired
    private MasterMailRepository masterMailRepository;

    @Override
    public MasterMailVo findByIdAndNoDelete(Integer id) {
        return masterMailRepository.findByIdAndDeleteFlg(id, false);
    }
}