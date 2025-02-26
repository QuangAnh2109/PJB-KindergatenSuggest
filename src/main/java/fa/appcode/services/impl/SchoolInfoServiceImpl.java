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
    public List<SchoolInfo> findSchoolInfoListByAccountEmail(String email) {
        return schoolInfoRepository.findSchoolInfoByAccountEmail(email);
    }
    @Override
    public List<SchoolInfo> findAllSchoolPublished() {
        return schoolInfoRepository.findAllSchoolPublished();
    }

    @Override
    public SchoolInfo getSchoolInfoById(int email) {
        return schoolInfoRepository.findSchoolInfoById(email);
    }

    @Override
    public List<Integer> getAllSchoolIdsByAccountEmail(String email) {
        return schoolInfoRepository.getAllSchoolIdsByAccountEmail(email);
    }

    @Override
    public List<Integer> getAllSchoolIdsForUnenrollParentByAccountEmail(String email) {
        return schoolInfoRepository.getAllSchoolIdsForUnenrollParentByAccountEmail(email);
    }
}
