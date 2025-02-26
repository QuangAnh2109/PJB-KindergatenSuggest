package fa.appcode.services.impl;

import fa.appcode.entities.SchoolInfo;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolInfoServiceImpl implements SchoolInfoService {

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;


    @Override
    public List<SchoolInfo> findSchoolInfoByAccountId(String id) {
        return schoolInfoRepository.findSchoolInfoByAccountEmail(id);
    }
    @Override
    public List<SchoolInfo> findAll() {
        return schoolInfoRepository.findAll();
    }

    @Override
    public SchoolInfo getSchoolInfoById(int id) {
        return schoolInfoRepository.findSchoolInfoById(id);
    }

    @Override
    public List<Integer> getAllSchoolIdsByAccountEmail(String id) {
        return schoolInfoRepository.getAllSchoolIdsByAccountEmail(id);
    }
}
