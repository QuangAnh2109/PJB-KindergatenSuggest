package fa.appcode.services;

import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.common.vo.SchoolListManager;
import fa.appcode.entities.SchoolInfo;

import java.util.List;

public interface SchoolInfoService {
    List<SchoolInfo> findSchoolInfoListByAccountEmail(String email);

    List<SchoolInfo> findAllSchoolPublished();

    SchoolInfo getSchoolInfoById(int id);

    List<Integer> getAllSchoolIdsByAccountEmail(String email);

    List<Integer> getAllSchoolIdsForUnenrollParentByAccountEmail(String email);

    List<SchoolListManager> searchAllByNameAndPagingAndDeleteFlg(int page, String search);

    List<SchoolListManager> searchAllByNameAndAccountAndPagingAndDeleteFlg(int page, String search, String email);

    int updateSchoolStatusByRequest(int id, int recordNo, int schoolStatus, String updateId, List<Integer> list);

    int updateSchoolStatusByRequestAndAccount(int id, String email, int recordNo, int schoolStatus, String updateId, List<Integer> list);

    SchoolFormManager getSchoolFormByIdAndNoDelete(int id);

    int updateSchoolInfoBySchoolFormManager(SchoolFormManager schoolFormManager);
}
