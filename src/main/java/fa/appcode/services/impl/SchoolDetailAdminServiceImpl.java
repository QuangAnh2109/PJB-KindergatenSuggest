package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.RoleConstant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.services.SchoolDetailAdminService;
import fa.appcode.services.SchoolInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolDetailAdminServiceImpl implements SchoolDetailAdminService {

    private final SchoolInfoService schoolInfoService;

    @Override
    public boolean deleteSchoolByStatus(int id, int record) {
        // Check school status is submitted, approved, rejected, published, unpublished
        List<Integer> inStatus = List.of(SchoolConstant.STATUS_SAVED, SchoolConstant.STATUS_SUBMITTED, SchoolConstant.STATUS_APPROVED, SchoolConstant.STATUS_REJECTED, SchoolConstant.STATUS_PUBLISHED, SchoolConstant.STATUS_UNPUBLISHED);
        // Update school status to deleted and return true if success
        return schoolInfoService.updateSchoolStatusByRequest(id, record, SchoolConstant.STATUS_DELETED, RoleConstant.ADMIN, inStatus) > 0;
    }

    @Override
    public boolean setButtonSchoolDetailFormToModel(Model model, int id, boolean edit) {
        return false;
    }
}