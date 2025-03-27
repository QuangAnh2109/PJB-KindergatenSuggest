package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SortOption;
import fa.appcode.common.vo.*;
import fa.appcode.config.GlobalConfig;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/public")
public class UserHomeController {

    private final CityService cityService;
    private final DistrictService districtService;
    private final MasterDatumService masterDatumService;
    private final GlobalConfig globalConfig;
    private final SchoolInfoService schoolInfoService;
    private final Logger logger = LoggerFactory.getLogger(UserHomeController.class);
    private final EnrollSchoolService enrollSchoolService;

    @GetMapping(Constant.HOME_PAGE_URL)
    public String parentHome(Model model) {
        List<CityVo> listCityVo = cityService.findAllByNoDelete();
        HomeVo homeVo = schoolInfoService.dataHomePage();
        model.addAttribute("listCity", listCityVo);
        model.addAttribute("dataHome", homeVo);
        return Constant.HOME_PAGE;
    }

    @GetMapping("/districts")
    public ResponseEntity<?> getDistrictByCity(@RequestParam(name = "cityId") Integer cityId) {
        try {
            List<DistrictVo> districts = districtService.findAllByCityIdAndNoDelete(cityId);
            return ResponseEntity.ok(districts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch districts: " + e.getMessage());
        }
    }

    @GetMapping("/school/search")
    public String showSearchResults(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Integer districtId,
            Model model,
            @RequestParam(defaultValue = Constant.INIT_PAGE) int page,
            @RequestParam(defaultValue = Constant.PAGE_SIZE) int size,
            @RequestParam(defaultValue = "BY_RATING_DESC") SortOption sortBy) {

        //Init data
        loadCommonData(model);

        // Load pageable
        Pageable pageable = PageRequest.of(page, size, sortBy.toSort());

        //Search
        Page<MySchoolVo> listSearchResult = schoolInfoService.searchSchoolInfoByCategories(keyword,cityId,districtId,pageable);
        Map<Integer,List<String>> listFacilitiesOfSchool = enrollSchoolService.getFacilitiesMapForSchools(listSearchResult);

        //Response
        model.addAttribute("listSearch", listSearchResult)
                .addAttribute("keyword", keyword)
                .addAttribute("cityId", cityId)
                .addAttribute("districtId", districtId)
                .addAttribute("currentPage", page)
                .addAttribute("totalPages", size)
                .addAttribute("listFacilities",listFacilitiesOfSchool)
                .addAttribute("sortOptions", SortOption.values())
                .addAttribute("currentSort", sortBy)
                .addAttribute("emailErrorMessage", globalConfig.getInValidEmail())
                .addAttribute("mobileErrorMessage", globalConfig.getInvalidPhoneNumber());

        return "user_side/search-school";
    }



    private void loadCommonData(Model model) {
        List<CityVo> listCity = cityService.findAllByNoDelete();
        List<MasterDataVo> listFacilities = masterDatumService.findAllByTypeNameNoDelete("FACILITIES");
        List<MasterDataVo> listTypeSchool = masterDatumService.findAllByTypeNameNoDelete("SCHOOL TYPE");
        List<MasterDataVo> listDataAge = masterDatumService.findAllByTypeNameNoDelete("CHILD RECEIVING AGE");
        List<MasterDataVo> listUtilities = masterDatumService.findAllByTypeNameNoDelete("UTILITIES");

        model.addAttribute("facilities", listFacilities);
        model.addAttribute("typeSchool", listTypeSchool);
        model.addAttribute("data_age", listDataAge);
        model.addAttribute("utilities", listUtilities);
        model.addAttribute("listCity", listCity);
        model.addAttribute("emailErrorMessage", globalConfig.getInvalidPhoneNumber());
        model.addAttribute("mobileErrorMessage", globalConfig.getInvalidPhoneNumber());
    }

}