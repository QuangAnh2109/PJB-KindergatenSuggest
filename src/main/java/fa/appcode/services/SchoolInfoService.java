package fa.appcode.services;

import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.common.vo.SchoolListManager;
import fa.appcode.entities.SchoolInfo;

import java.util.List;

public interface SchoolInfoService {
    /**
     * Retrieves a List of Published School based on School Owner email
     *
     * @return a List of SchoolInfo
     */
    List<SchoolInfo> findSchoolInfoListByAccountEmail(String email);

    /**
     * Retrieves a List of Published School
     *
     * @return a List of SchoolInfo
     */
    List<SchoolInfo> findAllSchoolPublished();

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

    List<SchoolListManager> searchAllByNameAndPagingAndDeleteFlg(int page, String search);

    List<SchoolListManager> searchAllByNameAndAccountAndPagingAndDeleteFlg(int page, String search, String email);

    int updateSchoolStatusByRequest(int id, int recordNo, int schoolStatus, String updateId, List<Integer> list);

    int updateSchoolStatusByRequestAndAccount(int id, String email, int recordNo, int schoolStatus, String updateId, List<Integer> list);

    SchoolFormManager getSchoolFormByIdAndNoDelete(int id);

    int updateSchoolInfoBySchoolFormManager(SchoolFormManager schoolFormManager);
}
