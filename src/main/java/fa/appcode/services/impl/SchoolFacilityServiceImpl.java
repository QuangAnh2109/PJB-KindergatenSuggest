package fa.appcode.services.impl;

import fa.appcode.common.utils.RoleConstant;
import fa.appcode.entities.*;
import fa.appcode.repositories.SchoolFacilityRepository;
import fa.appcode.services.SchoolFacilityService;
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
public class SchoolFacilityServiceImpl implements SchoolFacilityService {
    private final SchoolFacilityRepository schoolFacilityRepository;

    @Override
    public List<Integer> getAllSchoolFacilityIdBySchoolIdAndNoDeleteFlg(int schoolId) {
        return schoolFacilityRepository.getAllSchoolFacilityIdBySchoolId(schoolId, false);
    }

    @Override
    @Transactional
    public int saveAllSchoolFacility(List<Integer> schoolFacilityId, SchoolInfo schoolInfo) throws DataAccessException {
        List<SchoolFacility> currentFacilities = schoolFacilityRepository.findBySchoolId(schoolInfo.getId());

        Set<Integer> currentIds = currentFacilities.stream()
                .map(sf -> sf.getId().getFacilitiesId())
                .collect(Collectors.toSet());
        Set<Integer> newIds = new HashSet<>(schoolFacilityId);

        currentFacilities.stream()
                .filter(sf -> !newIds.contains(sf.getId().getFacilitiesId()))
                .forEach(schoolFacilityRepository::delete);

        List<SchoolFacility> newFacilities = schoolFacilityId.stream()
                .filter(id -> !currentIds.contains(id))
                .map(id -> {
                    SchoolFacility sf = new SchoolFacility();
                    sf.setId(new SchoolFacilityId(schoolInfo.getId(), id));
                    sf.setSchool(schoolInfo);
                    sf.setCreateId(schoolInfo.getUpdateId());
                    sf.setCreateTime(Instant.now());
                    sf.setUpdateId(schoolInfo.getUpdateId());
                    sf.setUpdateTime(Instant.now());
                    sf.setDeleteFlg(false);
                    sf.setRecordNo(1);
                    return sf;
                })
                .collect(Collectors.toList());

        schoolFacilityRepository.saveAll(newFacilities);

        return newFacilities.size();
    }

}
