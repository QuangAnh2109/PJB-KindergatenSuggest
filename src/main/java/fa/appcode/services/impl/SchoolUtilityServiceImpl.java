package fa.appcode.services.impl;

import fa.appcode.repositories.SchoolUtilityRepository;
import fa.appcode.services.SchoolUtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolUtilityServiceImpl implements SchoolUtilityService {
    private final SchoolUtilityRepository schoolUtilityRepository;

    @Override
    public List<Integer> getAllSchoolUtilityIdBySchoolIdAndNoDelete(int schoolId) {
        return schoolUtilityRepository.getAllSchoolUtilityIdBySchoolId(schoolId, false);
    }
}
