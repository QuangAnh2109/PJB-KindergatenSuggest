package fa.appcode.services.impl;

import fa.appcode.common.vo.SchoolInfoVo;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.SchoolInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SchoolInfoServiceImpl implements SchoolInfoService {

    private final SchoolInfoRepository schoolInfoRepository;

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

    @Override
    public SchoolInfoVo findSchoolInfoVoByIdAndAccountIdNoDelete(int id, int accountId) {
        return schoolInfoRepository.findSchoolInfoVoByIdAndAccountIdAndDeleteFlg(id, accountId, false);
    }

    @Override
    public SchoolInfoVo findSchoolInfoVoByIdNoDelete(int id) {
        return schoolInfoRepository.findSchoolInfoVoByIdAndDeleteFlg(id, false);
    }

    @Override
    public SchoolInfo findSchoolInfoByIdAndAccountIdNoDelete(int id, int accountId) {
        return schoolInfoRepository.findSchoolInfoByIdAndAccountIdAndDeleteFlg(id, accountId, false);
    }

    @Override
    public SchoolInfo findSchoolInfoByIdNoDelete(int id) {
        return schoolInfoRepository.findSchoolInfoByIdAndDeleteFlg(id, false);
    }

    @Override
    public SchoolInfo save(SchoolInfo schoolInfo) {
        return schoolInfoRepository.save(schoolInfo);
    }
}
