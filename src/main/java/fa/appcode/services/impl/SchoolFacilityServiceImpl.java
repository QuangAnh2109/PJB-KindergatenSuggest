package fa.appcode.services.impl;

import fa.appcode.common.utils.RoleConstant;
import fa.appcode.entities.*;
import fa.appcode.repositories.SchoolFacilityRepository;
import fa.appcode.services.SchoolFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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
    public int saveAllSchoolFacility(List<Integer> schoolFacilityId, SchoolInfo schoolInfo) throws DataAccessException {
        return schoolFacilityRepository.saveAll(schoolFacilityId.stream()
                .map(facilityId -> {
                    SchoolFacility sf = new SchoolFacility();
                    sf.setId(new SchoolFacilityId(schoolInfo.getId(), facilityId));
                    sf.setSchool(schoolInfo);
                    sf.setCreateId(RoleConstant.SCHOOL_OWNER);
                    sf.setCreateTime(Instant.now());
                    sf.setUpdateId(RoleConstant.SCHOOL_OWNER);
                    sf.setUpdateTime(Instant.now());
                    sf.setDeleteFlg(false);
                    sf.setRecordNo(1);
                    return sf;
                })
                .collect(Collectors.toList())
        ).size();
    }
}
