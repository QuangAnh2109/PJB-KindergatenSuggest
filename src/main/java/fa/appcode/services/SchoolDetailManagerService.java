package fa.appcode.services;

import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.entities.SchoolInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface SchoolDetailManagerService {
    String getSchoolCreateFormToModel(Model model);
    void setAllAddressToModel(Model model, int cityId, int districtId);
    String getSchoolDetail(Model model, int schoolId);
    ResponseEntity<Map<String, Object>> changeSchoolStatus(Integer id, int recordNo, int newStatus, List<Integer> inStatus, Integer mailId, Map<Placeholder, String> detail, List<String> toMail, List<String> ccMail) throws DataAccessException;
    ResponseEntity<Map<String, Object>> createNewSchool(SchoolInfo schoolInfo, MultipartFile image, List<Integer> schoolFacilityId, List<Integer> schoolUtilityId) throws DataAccessException;
    ResponseEntity<Map<String, Object>> updateSchool(SchoolFormManager schoolInfo, MultipartFile image, List<Integer> schoolFacilityId, List<Integer> schoolUtilityId) throws DataAccessException;
}