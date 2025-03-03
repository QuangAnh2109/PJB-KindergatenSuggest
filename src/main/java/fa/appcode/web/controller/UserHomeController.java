package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.CityVo;
import fa.appcode.common.vo.DistrictVo;
import fa.appcode.common.vo.MasterDataVo;
import fa.appcode.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Controller
public class UserHomeController {

    private final CityService cityService;
    private final DistrictService districtService;
    private final MasterDatumService masterDatumService;

    @GetMapping(Constant.HOME_PAGE_URL)
    public String parentHome(Model model) {
        List<CityVo> listCity1 = cityService.findAllByNoDelete();
        model.addAttribute("listCity", listCity1);
        return Constant.HOME_PAGE;
    }

    @GetMapping("/public/districts")
    public ResponseEntity<?> getDistrictByCity(@RequestParam(name = "cityId") Integer cityId) {
        try {
            List<DistrictVo> districts = districtService.findAllByCityIdAndNoDelete(cityId);
            return ResponseEntity.ok(districts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch districts: " + e.getMessage());
        }
    }


    @GetMapping("/public/search")
    public String searchSchool(Model model) {
        List<CityVo> listCity1 = cityService.findAllByNoDelete();
        List<MasterDataVo> listFacilities = masterDatumService.findAllByTypeNameNoDelete("FACILITIES");
        List<MasterDataVo> listTypeSchool = masterDatumService.findAllByTypeNameNoDelete("SCHOOL TYPE");
        List<MasterDataVo> listDataAge = masterDatumService.findAllByTypeNameNoDelete("CHILD RECEIVING AGE");
        List<MasterDataVo> listUtilities = masterDatumService.findAllByTypeNameNoDelete("UTILITIES");
        
        //===========================================================
        model.addAttribute("facilities", listFacilities);
        model.addAttribute("type_school", listTypeSchool);
        model.addAttribute("data_age", listDataAge);
        model.addAttribute("utilities", listUtilities);
        model.addAttribute("listCity", listCity1);
        return "user_side/search-school";
    }
    @GetMapping("/public/search/results")
    public String showSearchResults(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer cityId,
                                    @RequestParam(required = false) Integer districtId,
                                    Model model) {

        // Thêm dữ liệu vào model để hiển thị trên trang
        model.addAttribute("keyword", keyword);
        model.addAttribute("cityId", cityId);
        model.addAttribute("districtId", districtId);

        return "user_side/search-school"; 
    }
}