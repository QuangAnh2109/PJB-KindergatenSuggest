package fa.appcode.services.impl;

import fa.appcode.common.utils.RoleConstant;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.entities.SchoolUtility;
import fa.appcode.entities.SchoolUtilityId;
import fa.appcode.repositories.SchoolUtilityRepository;
import fa.appcode.services.SchoolUtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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
    public int saveAllSchoolUtility(List<Integer> schoolUtilityId, SchoolInfo schoolInfo) throws DataAccessException {
        return schoolUtilityRepository.saveAll(schoolUtilityId.stream()
                .map(utilityId -> {
                    SchoolUtility su = new SchoolUtility();
                    su.setId(new SchoolUtilityId(schoolInfo.getId(), utilityId));
                    su.setSchool(schoolInfo);
                    su.setCreateId(RoleConstant.SCHOOL_OWNER);
                    su.setCreateTime(Instant.now());
                    su.setUpdateId(RoleConstant.SCHOOL_OWNER);
                    su.setUpdateTime(Instant.now());
                    su.setDeleteFlg(false);
                    su.setRecordNo(1);
                    return su;
                })
                .collect(Collectors.toList())
        ).size();
    }
}
