package fa.appcode.services;

import fa.appcode.common.vo.SchoolInfoVo;
import fa.appcode.entities.SchoolInfo;

import java.util.List;

public interface SchoolInfoService {
    List<SchoolInfo> findSchoolInfoListByAccountEmail(String email);

    List<SchoolInfo> findAllSchoolPublished();

    SchoolInfo getSchoolInfoById(int id);

    List<Integer> getAllSchoolIdsByAccountEmail(String email);

    List<Integer> getAllSchoolIdsForUnenrollParentByAccountEmail(String email);

    SchoolInfoVo findSchoolInfoVoByIdAndAccountIdNoDelete(int id, int accountId);

    SchoolInfoVo findSchoolInfoVoByIdNoDelete(int id);

    SchoolInfo findSchoolInfoByIdAndAccountIdNoDelete(int id, int accountId);

    SchoolInfo findSchoolInfoByIdNoDelete(int id);

    SchoolInfo save(SchoolInfo schoolInfo);
}
