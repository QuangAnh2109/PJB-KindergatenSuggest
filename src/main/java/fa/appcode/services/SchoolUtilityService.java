package fa.appcode.services;

import java.util.List;

public interface SchoolUtilityService {
    List<Integer> getAllSchoolUtilityIdBySchoolIdAndNoDelete(int schoolId);
}