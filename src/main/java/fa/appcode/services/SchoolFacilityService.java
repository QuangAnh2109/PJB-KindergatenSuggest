package fa.appcode.services;

import fa.appcode.entities.SchoolFacility;
import fa.appcode.entities.SchoolInfo;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface SchoolFacilityService {
    List<Integer> getAllSchoolFacilityIdBySchoolIdAndNoDeleteFlg(int schoolId);
    int saveAllSchoolFacility(List<Integer> schoolUtilityId, SchoolInfo schoolInfo) throws DataAccessException;
}
