package fa.appcode.services;

import fa.appcode.entities.SchoolInfo;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface SchoolUtilityService {
    List<Integer> getAllSchoolUtilityIdBySchoolIdAndNoDelete(int schoolId);
    void saveAllSchoolUtility(List<Integer> schoolUtilityId, SchoolInfo schoolInfo) throws DataAccessException;
}