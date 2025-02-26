package fa.appcode.services;

import fa.appcode.entities.SchoolInfo;

import java.util.List;

public interface SchoolInfoService {
    List<SchoolInfo> findSchoolInfoByAccountId(String id);
    List<SchoolInfo> findAll();
    SchoolInfo getSchoolInfoById(int id);
    List<Integer> getAllSchoolIdsByAccountEmail(String id);
}
