package fa.appcode.services.impl;

import fa.appcode.entities.SchoolInfo;
import fa.appcode.entities.SchoolUtility;
import fa.appcode.entities.SchoolUtilityId;
import fa.appcode.repositories.SchoolUtilityRepository;
import fa.appcode.services.SchoolUtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolUtilityServiceImpl implements SchoolUtilityService {
    private final SchoolUtilityRepository schoolUtilityRepository;

    @Override
    public List<Integer> getAllSchoolUtilityIdBySchoolIdAndNoDelete(int schoolId) {
        return schoolUtilityRepository.getAllSchoolUtilityIdBySchoolId(schoolId, false);
    }

    @Override
    @Transactional
    public void saveAllSchoolUtility(List<Integer> schoolUtilityId, SchoolInfo schoolInfo) throws DataAccessException {
        if (schoolUtilityId == null || schoolUtilityId.isEmpty()) return;

        List<SchoolUtility> currentUtilities = schoolUtilityRepository.findBySchoolId(schoolInfo.getId());

        Set<Integer> currentIds = currentUtilities.stream()
                .map(su -> su.getId().getUtilitiesId())
                .collect(Collectors.toSet());
        Set<Integer> newIds = new HashSet<>(schoolUtilityId);

        currentUtilities.stream()
                .filter(su -> !newIds.contains(su.getId().getUtilitiesId()))
                .forEach(schoolUtilityRepository::delete);

        List<SchoolUtility> newUtilities = schoolUtilityId.stream()
                .filter(id -> !currentIds.contains(id))
                .map(id -> {
                    SchoolUtility su = new SchoolUtility();
                    su.setId(new SchoolUtilityId(schoolInfo.getId(), id));
                    su.setSchool(schoolInfo);
                    su.setCreateId(schoolInfo.getUpdateId());
                    su.setCreateTime(Instant.now());
                    su.setUpdateId(schoolInfo.getUpdateId());
                    su.setUpdateTime(Instant.now());
                    su.setDeleteFlg(false);
                    su.setRecordNo(1);
                    return su;
                })
                .collect(Collectors.toList());

        schoolUtilityRepository.saveAll(newUtilities);
    }

}
