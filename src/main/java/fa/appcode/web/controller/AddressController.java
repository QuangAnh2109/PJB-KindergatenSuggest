package fa.appcode.web.controller;

import fa.appcode.services.impl.DistrictServiceImpl;
import fa.appcode.services.impl.MasterDatumServiceImpl;
import fa.appcode.services.impl.WardServiceImpl;
import fa.appcode.vo.DistrictVo;
import fa.appcode.vo.WardVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AddressController {
    @Autowired
    private DistrictServiceImpl districtServiceImpl;

    @Autowired
    private WardServiceImpl wardServiceImpl;

    @GetMapping("/admin/school-form/district")
    List<DistrictVo> getDistrictByCity(@RequestParam(name = "cityId") Integer cityId){
        return districtServiceImpl.findAllByCityIdAndDeleteFlg(cityId, false);
    }
    @GetMapping("/admin/school-form/ward")
    List<WardVo> getWardByDistrict(@RequestParam(name = "districtId") Integer districtId){
        return wardServiceImpl.findAllByDistrictIdAndDeleteFlg(districtId, false);
    }
}
