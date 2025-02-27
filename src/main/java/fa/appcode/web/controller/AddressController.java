package fa.appcode.web.controller;

import fa.appcode.services.WardService;
import fa.appcode.services.DistrictService;
import fa.appcode.common.vo.DistrictVo;
import fa.appcode.common.vo.WardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class AddressController {
    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;

    @GetMapping("/district")
    List<DistrictVo> getDistrictByCity(@RequestParam(name = "cityId") Integer cityId){
        return districtService.findAllByCityIdAndNoDelete(cityId);
    }
    @GetMapping("/ward")
    List<WardVo> getWardByDistrict(@RequestParam(name = "districtId") Integer districtId){
        return wardService.findAllByDistrictIdAndNoDelete(districtId);
    }
}
