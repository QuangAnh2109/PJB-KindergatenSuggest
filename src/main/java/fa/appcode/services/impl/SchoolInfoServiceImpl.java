package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.*;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.exceptions.TokenException;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.SchoolInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SchoolInfoServiceImpl implements SchoolInfoService {

    private final SchoolInfoRepository schoolInfoRepository;

    private final Logger logger = Logger.getLogger(SchoolInfoServiceImpl.class.getName());

    private final GlobalConfig globalConfig;

    @Override
    public List<EnrollSchoolInfoVo> findSchoolInfoListByAccountEmail(String email) {
        return schoolInfoRepository.findSchoolInfoByAccountEmail(email,Constant.SCHOOL_PUBLISH_STATUS);
    }


    @Override
    public SchoolInfo getSchoolInfoById(int email) {
        return schoolInfoRepository.findSchoolInfoById(email,Constant.SCHOOL_PUBLISH_STATUS);
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


    @Override
    public Page<MySchoolVo> searchSchoolInfoByCategories(String keyword, Integer cityId, Integer districtId,
                                                         Integer schoolType, Integer admissionAge, Double minFee,
                                                         Double maxFee,List<Integer> facilities,List<Integer> utilities,Pageable pageable) throws RuntimeException{
        Page<MySchoolVo> listResultSearchSchool = schoolInfoRepository.searchSchoolInfoByCategories(keyword, cityId, districtId,
                                                    schoolType,admissionAge,minFee,maxFee,facilities,utilities, pageable);
        logger.info("Found " + listResultSearchSchool.getTotalElements() + " School Infos");
        return listResultSearchSchool;
    }

    @Override
    public Page<MySchoolVo> searchSchoolInfoByCategoriesAndSortBy(String keyword, Integer cityId, Integer districtId, Integer schoolType, Integer admissionAge, Double minFee, Double maxFee, List<Integer> facilities, List<Integer> utilities, Pageable pageable) {
        Page<MySchoolVo> listResultSearchSchool = schoolInfoRepository.searchSchoolInfoByCategoriesAndSortBy(keyword, cityId, districtId,
                schoolType,admissionAge,minFee,maxFee,facilities,utilities, pageable);
        logger.info("Found " + listResultSearchSchool.getTotalElements() + " School Infos");
        return listResultSearchSchool;
    }

    @Override
    public HomeVo dataHomePage() {
        return schoolInfoRepository.dataHomePage();
    }

    @Override
    public MySchoolVo findSchoolDetailBySchoolId(int schoolId) {
        MySchoolVo schoolInfo = schoolInfoRepository.findSchoolDetailBySchoolId(schoolId);
        if(schoolInfo.getSchoolId() != null) {
            return schoolInfo;
        }else {
            throw new RuntimeException("School not found or unpublish");
        }
    }
}
