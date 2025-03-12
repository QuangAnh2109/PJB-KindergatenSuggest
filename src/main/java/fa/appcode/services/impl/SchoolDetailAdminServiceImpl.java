package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.RoleConstant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.utils.SchoolFormButton;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.common.vo.SchoolStatusUpdateRequest;
import fa.appcode.repositories.*;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolDetailAdminService;
import fa.appcode.services.SchoolDetailManagerService;
import fa.appcode.services.SchoolInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.List;

import static fa.appcode.common.utils.Constant.EMAIL_REGEX_HTML;
import static fa.appcode.common.utils.Constant.PHONE_REGEX_HTML;

@Service
@RequiredArgsConstructor
public class SchoolDetailAdminServiceImpl implements SchoolDetailAdminService {

    private final SchoolInfoRepository schoolInfoRepository;

    private final SchoolDetailManagerService schoolDetailManagerService;

    private final MasterDatumRepository masterDatumRepository;

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;

    private final WardRepository wardRepository;

    private final SchoolFacilityRepository schoolFacilityRepository;

    private final SchoolUtilityRepository schoolUtilityRepository;

    @Override
    public boolean deleteSchoolByStatus(int id, int recordNo) {
        // Check school status is submitted, approved, rejected, published, unpublished
        List<Integer> inStatus = List.of(SchoolConstant.STATUS_SAVED, SchoolConstant.STATUS_SUBMITTED, SchoolConstant.STATUS_APPROVED, SchoolConstant.STATUS_REJECTED, SchoolConstant.STATUS_PUBLISHED, SchoolConstant.STATUS_UNPUBLISHED);

        // Update school status to deleted and return true if success
        return schoolInfoRepository.updateSchoolStatusByRequest(SchoolStatusUpdateRequest.builder().id(id).recordNo(recordNo).schoolStatus(SchoolConstant.STATUS_DELETED).statusList(inStatus).updateId(RoleConstant.ADMIN).updateTime(Instant.now()).build()) > 0;
    }

    @Override
    public String getSchoolDetail(Model model, int schoolId) {

        SchoolFormManager school = schoolInfoRepository.getSchoolFormByIdAndDeleteFlg(schoolId, false);
        SchoolFormButton.valueOf("admin"+school.getStatusId());
        model.addAttribute("school", school);
        schoolDetailManagerService.setSchoolViewDetailFormToModel(model, school.getCityId(), school.getDistrictId());

        // Add regex to model
        model.addAttribute("emailRegex", EMAIL_REGEX_HTML);
        model.addAttribute("phoneRegex", PHONE_REGEX_HTML);

        model.addAttribute("statusName", masterDatumRepository.getMasterByTypeNameAndTypeKey(SchoolConstant.SCHOOL_STATUS, school.getStatusId()));

        model.addAttribute("schoolFacilities", schoolFacilityRepository.getAllSchoolFacilityIdBySchoolId(school.getId(), false));
        model.addAttribute("schoolUtilities", schoolUtilityRepository.getAllSchoolUtilityIdBySchoolId(school.getId(), false));

        // Add city list to model
        model.addAttribute("citys", cityRepository.findAllByDeleteFlg(false));
        return Constant.SCHOOL_DETAIL_MANAGER_PAGE;
    }
}