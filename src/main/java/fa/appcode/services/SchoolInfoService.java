package fa.appcode.services;

import fa.appcode.common.vo.*;
import fa.appcode.entities.SchoolInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchoolInfoService {
    /**
     * Retrieves a List of Published School based on School Owner email
     *
     * @return a List of SchoolInfo
     */
    List<EnrollSchoolInfoVo> findSchoolInfoListByAccountEmail(String email);


    /**
     * This method is used to find School information based on School ID
     *
     * @param id
     * @return a List of SchoolInfo
     */
    SchoolInfo getSchoolInfoById(int id);

    List<Integer> getAllSchoolIdsByAccountEmail(String email);

    /**
     * This method to find all School ID of School owner based on School Owner email
     *
     * @param email
     * @return a List of SchoolID
     */
    List<Integer> getAllSchoolIdsForUnenrollParentByAccountEmail(String email);

    Page<SchoolListManager> searchAllByNameAndPagingAndDeleteFlg(int page, String search);

    Page<SchoolListManager> searchAllByNameAndAccountAndPagingAndDeleteFlg(int page, String search, String email);

    int updateSchoolStatusByRequest(int id, int recordNo, int schoolStatus, String updateId, List<Integer> list);

    int updateSchoolStatusByRequestAndAccount(int id, String email, int recordNo, int schoolStatus, String updateId, List<Integer> list);

    SchoolFormManager getSchoolFormByIdAndNoDelete(int id);

    int updateSchoolInfoBySchoolFormManager(SchoolFormManager schoolFormManager);

    String getSchoolNameBySchoolIdAndNoDelete(int id);

    SchoolFormManager getSchoolFormBySchoolIdAndEmailAndNoDelete(int id, String email);

    public Page<MySchoolVo> searchSchoolInfoByCategories(String keyword, Integer cityId, Integer districtId,
                                                         Integer schoolType, Integer admissionAge, Double minFee,
                                                         Double maxFee,List<Integer> facilities,List<Integer> utilities,Pageable pageable);

    public Page<MySchoolVo> searchSchoolInfoByCategoriesAndSortBy(String keyword, Integer cityId, Integer districtId,
                                                         Integer schoolType, Integer admissionAge, Double minFee,
                                                         Double maxFee,List<Integer> facilities,List<Integer> utilities,Pageable pageable);

    HomeVo dataHomePage();

    MySchoolVo findSchoolDetailBySchoolId(int schoolId);
}
