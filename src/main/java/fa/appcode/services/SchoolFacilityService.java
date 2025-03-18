package fa.appcode.services;

import java.util.List;

public interface SchoolFacilityService {
    List<Integer> getAllSchoolFacilityIdBySchoolIdAndNoDeleteFlg(int schoolId);
}
