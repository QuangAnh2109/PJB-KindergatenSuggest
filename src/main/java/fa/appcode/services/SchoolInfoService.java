package fa.appcode.services;

import fa.appcode.entities.SchoolInfo;

import java.util.List;

public interface SchoolInfoService {
    List<SchoolInfo> findSchoolInfoListByAccountEmail(String email);
    List<SchoolInfo> findAllSchoolPublished();
    SchoolInfo getSchoolInfoById(int id);
    List<Integer> getAllSchoolIdsByAccountEmail(String email);
    List<Integer> getAllSchoolIdsForUnenrollParentByAccountEmail(String email);
}
