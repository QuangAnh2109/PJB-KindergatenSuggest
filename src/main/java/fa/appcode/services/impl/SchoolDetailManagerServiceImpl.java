package fa.appcode.services.impl;

import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.repositories.CityRepository;
import fa.appcode.repositories.DistrictRepository;
import fa.appcode.repositories.WardRepository;
import fa.appcode.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolDetailManagerServiceImpl implements SchoolDetailManagerService {

    private final CityRepository cityRepository;

    private final WardRepository wardRepository;

    private final DistrictRepository districtRepository;

    private final MasterDatumService masterDatumService;

    public void setSchoolUpdateFormToModel(Model model) {
        model.addAttribute("citys", cityRepository.findAllByDeleteFlg(false));
        masterDatumService.setMasterDataToModel(model);
    }

    public void setSchoolViewDetailFormToModel(Model model, int cityId, int districtId) {
        model.addAttribute("citys", cityRepository.findAllByDeleteFlg(false));
        model.addAttribute("districts", districtRepository.findAllByCityIdAndDeleteFlg(cityId, false));
        model.addAttribute("wards", wardRepository.findAllByDistrictIdAndDeleteFlg(districtId, false));
        masterDatumService.setMasterDataToModel(model);
    }
}
