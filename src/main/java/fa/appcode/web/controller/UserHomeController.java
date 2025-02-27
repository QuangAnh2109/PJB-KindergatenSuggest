package fa.appcode.web.controller;

import fa.appcode.common.vo.CityVo;
import fa.appcode.common.vo.DistrictVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.City;
import fa.appcode.services.CityService;
import fa.appcode.services.DistrictService;
import lombok.AllArgsConstructor;
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
    @GetMapping("/public/home")
    public String parentHome(Model model) {
        List<CityVo> listCity1 = cityService.findAllByNoDelete();
        model.addAttribute("listCity", listCity1);
        return "user_side/index";
    }

    @GetMapping("/get-districts")
    public String getDistricts(@RequestParam("cityId") Integer cityId, Model model) {
        List<DistrictVo> districts = districtService.findAllByCityIdAndNoDelete(cityId);
        model.addAttribute("districts", districts);
        return "fragments/district-options";
    }


    @GetMapping("/search")
    public String searchSchool(Model model) {
        List<CityVo> listCity1 = cityService.findAllByNoDelete();
        model.addAttribute("listCity", listCity1);
        return "user_side/search-school";
    }


}