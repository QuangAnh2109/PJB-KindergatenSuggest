package fa.appcode.services.impl;

import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolInfoServiceImpl implements SchoolInfoService {

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;
}
