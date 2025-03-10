package fa.appcode.services.impl;

import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.services.CityService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolDetailManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolDetailManagerServiceImpl implements SchoolDetailManagerService {

    private final CityService cityService;

    private final MasterDatumService masterDatumService;

    public void setSchoolUpdateFormToModel(Model model, boolean edit, String title1, String title2) {
        model.addAttribute("edit", edit);
        model.addAttribute("title1", title1);
        model.addAttribute("title2", title2);
        model.addAttribute("citys", cityService.findAllByNoDelete());

        // Get all master data by type name(SCHOOL TYPE, CHILD RECEIVING AGE, EDUCATION METHOD, FACILITIES, UTILITIES) in no delete
        List<MasterDataVo> masterDataVoList = masterDatumService.findAllByTypeNameInNoDelete(List.of(SchoolConstant.SCHOOL_TYPE, SchoolConstant.CHILD_RECEIVING_AGE, SchoolConstant.EDUCATION_METHOD, SchoolConstant.FACILITIES, SchoolConstant.UTILITIES));

        // Create list to store each type of master data
        List<MasterDataVo> schoolTypes = new ArrayList<>(), childReceivingAges = new ArrayList<>(), educationMethods = new ArrayList<>(), facilities = new ArrayList<>(), utilities = new ArrayList<>();
        for (MasterDataVo vo : masterDataVoList) {
            switch (vo.getTypeName()) {
                case SchoolConstant.SCHOOL_TYPE:
                    schoolTypes.add(vo);
                    break;
                case SchoolConstant.CHILD_RECEIVING_AGE:
                    childReceivingAges.add(vo);
                    break;
                case SchoolConstant.EDUCATION_METHOD:
                    educationMethods.add(vo);
                    break;
                case SchoolConstant.FACILITIES:
                    facilities.add(vo);
                    break;
                case SchoolConstant.UTILITIES:
                    utilities.add(vo);
                    break;
            }
        }

        // Add all master data to model
        model.addAttribute("schoolTypes", schoolTypes);
        model.addAttribute("childReceivingAges", childReceivingAges);
        model.addAttribute("educationMethods", educationMethods);
        model.addAttribute("facilities", facilities);
        model.addAttribute("utilities", utilities);

        // Set button for school form
    }
}
