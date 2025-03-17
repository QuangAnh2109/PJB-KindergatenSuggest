package fa.appcode.services.impl;

import fa.appcode.common.vo.EnrollSchoolInfoVo;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.common.vo.SchoolListManager;
import fa.appcode.common.vo.SchoolStatusUpdateRequest;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.SchoolInfoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolInfoServiceImpl implements SchoolInfoService {

    private final SchoolInfoRepository schoolInfoRepository;

    private final GlobalConfig globalConfig;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EnrollSchoolInfoVo> findSchoolInfoListByAccountEmail(String email) {
        return schoolInfoRepository.findSchoolInfoByAccountEmail(email);
    }
    @Override
    public List<EnrollSchoolInfoVo> findAllSchoolPublished() {
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

    //find all SchoolListManager by paging and search and delete flag
    @Override
    public Page<SchoolListManager> searchAllByNameAndPagingAndDeleteFlg(int page, String search) {
        return schoolInfoRepository.searchAllByNameAndPagingAndDeleteFlg(PageRequest.of(page, globalConfig.getSizeOfPage()), search, false);
    }

    //find all SchoolListManager by paging and search and account and delete flag
    @Override
    public Page<SchoolListManager> searchAllByNameAndAccountAndPagingAndDeleteFlg(int page, String search, String email) {
        return schoolInfoRepository.searchAllByNameAndAccountAndPagingAndDeleteFlg(PageRequest.of(page, globalConfig.getSizeOfPage()), search, email, false);
    }

    //update school status by school id and record no and no delete
    @Override
    @Transactional
    public int updateSchoolStatusByRequest(int id, int recordNo, int schoolStatus, String updateId, List<Integer> list) {
        return schoolInfoRepository.updateSchoolStatusByRequest(SchoolStatusUpdateRequest.builder().id(id).recordNo(recordNo).schoolStatus(schoolStatus).statusList(list).updateId(updateId).updateTime(Instant.now()).build());
    }

    @Override
    @Transactional
    public int updateSchoolStatusByRequestAndAccount(int id, String email, int recordNo, int schoolStatus, String updateId, List<Integer> list) {
        return schoolInfoRepository.updateSchoolStatusByRequest(SchoolStatusUpdateRequest.builder().id(id).recordNo(recordNo).email(email).schoolStatus(schoolStatus).statusList(list).updateId(updateId).updateTime(Instant.now()).build());
    }

    @Override
    public SchoolFormManager getSchoolFormByIdAndNoDelete(int id) {
        return schoolInfoRepository.getSchoolFormByIdAndDeleteFlg(id, false);
    }

    @Override
    public int updateSchoolInfoBySchoolFormManager(SchoolFormManager schoolFormManager) {
        return schoolInfoRepository.updateSchoolInfoBySchoolFormManager(schoolFormManager);
    }

    @Override
    public String getSchoolNameBySchoolIdAndNoDelete(int id) {
        return schoolInfoRepository.getSchoolNameByIdAndDeleteFlg(id, false);
    }

    @Override
    public SchoolFormManager getSchoolFormBySchoolIdAndEmailAndNoDelete(int id, String email) {
        return schoolInfoRepository.getSchoolFormBySchoolIdAndEmailAndDeleteFlg(id, email, false);
    }
}
