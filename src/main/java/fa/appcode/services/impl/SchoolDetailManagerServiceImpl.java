package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.repositories.CityRepository;
import fa.appcode.repositories.DistrictRepository;
import fa.appcode.repositories.WardRepository;
import fa.appcode.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static fa.appcode.common.utils.Constant.EMAIL_REGEX_HTML;
import static fa.appcode.common.utils.Constant.PHONE_REGEX_HTML;

@Service
@RequiredArgsConstructor
public class SchoolDetailManagerServiceImpl implements SchoolDetailManagerService {

    private final CityRepository cityRepository;

    private final WardRepository wardRepository;

    private final DistrictRepository districtRepository;

    private final MasterDatumService masterDatumService;

    private final GlobalConfig globalConfig;

    @Override
    public String getSchoolCreateFormToModel(Model model) {
        model.addAttribute("citys", cityRepository.findAllByDeleteFlg(false));
        // Add regex to model
        model.addAttribute("emailRegex", EMAIL_REGEX_HTML);
        model.addAttribute("phoneRegex", PHONE_REGEX_HTML);
        model.addAttribute("serverLink", globalConfig.getServerLink());
        masterDatumService.setMasterDataToModel(model);
        return Constant.SCHOOL_CREATE_PAGE;
    }

    @Override
    public void setSchoolViewDetailFormToModel(Model model, int cityId, int districtId) {
        model.addAttribute("citys", cityRepository.findAllByDeleteFlg(false));
        model.addAttribute("districts", districtRepository.findAllByCityIdAndDeleteFlg(cityId, false));
        model.addAttribute("wards", wardRepository.findAllByDistrictIdAndDeleteFlg(districtId, false));
        masterDatumService.setMasterDataToModel(model);
    }
}
