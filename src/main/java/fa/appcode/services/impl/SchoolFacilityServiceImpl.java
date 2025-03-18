package fa.appcode.services.impl;

import fa.appcode.repositories.SchoolFacilityRepository;
import fa.appcode.services.SchoolFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolFacilityServiceImpl implements SchoolFacilityService {
    private final SchoolFacilityRepository schoolFacilityRepository;

    @Override
    public List<Integer> getAllSchoolFacilityIdBySchoolIdAndNoDeleteFlg(int schoolId) {
        return schoolFacilityRepository.getAllSchoolFacilityIdBySchoolId(schoolId, false);
    }
}
