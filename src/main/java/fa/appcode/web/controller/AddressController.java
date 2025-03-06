package fa.appcode.web.controller;

import fa.appcode.common.vo.CityVo;
import fa.appcode.services.CityService;
import fa.appcode.services.WardService;
import fa.appcode.services.DistrictService;
import fa.appcode.common.vo.DistrictVo;
import fa.appcode.common.vo.WardVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class AddressController {

    private final CityService cityService;

    private final DistrictService districtService;

    private final WardService wardService;

    @GetMapping("/district")
    List<DistrictVo> getDistrictByCity(@RequestParam(name = "cityId") Integer cityId) {
        return districtService.findAllByCityIdAndNoDelete(cityId);
    }

    @GetMapping("/ward")
    List<WardVo> getWardByDistrict(@RequestParam(name = "districtId") Integer districtId) {
        return wardService.findAllByDistrictIdAndNoDelete(districtId);
    }

    @GetMapping("/city/{id}")
    CityVo getCityById(@PathVariable("id") int id) {
        return cityService.findByIdAndNoDelete(id);
    }

    @GetMapping("/district/{id}")
    DistrictVo getDistrictById(@PathVariable("id") int id) {
        return districtService.findByIdAndNoDelete(id);
    }

    @GetMapping("/ward/{id}")
    WardVo getWardById(@PathVariable("id") int id) {
        return wardService.findByIdAndNoDelete(id);
    }
}
