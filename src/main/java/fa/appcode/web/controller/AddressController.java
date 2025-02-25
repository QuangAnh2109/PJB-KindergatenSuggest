package fa.appcode.web.controller;

import fa.appcode.services.impl.DistrictServiceImpl;
import fa.appcode.services.impl.WardServiceImpl;
import fa.appcode.common.vo.DistrictVo;
import fa.appcode.common.vo.WardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class AddressController {
    @Autowired
    private DistrictServiceImpl districtServiceImpl;

    @Autowired
    private WardServiceImpl wardServiceImpl;

    @GetMapping("/admin/school-form/district")
    List<DistrictVo> getDistrictByCity(@RequestParam(name = "cityId") Integer cityId){
        return districtServiceImpl.findAllByCityIdAndNoDelete(cityId);
    }
    @GetMapping("/admin/school-form/ward")
    List<WardVo> getWardByDistrict(@RequestParam(name = "districtId") Integer districtId){
        return wardServiceImpl.findAllByDistrictIdAndNoDelete(districtId);
    }
}
