package fa.appcode.web.controller;

import fa.appcode.common.vo.SearchVo;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.services.SchoolInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/public/search")
@AllArgsConstructor
public class SearchRestController {

    private final SchoolInfoService schoolService;

    @GetMapping("/process")
    public ResponseEntity<Void> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Integer districtId) {

        String redirectUrl = "/public/school/search?keyword=" + (keyword != null ? keyword : "") +
                (cityId != null ? "&cityId=" + cityId : "") +
                (districtId != null ? "&districtId=" + districtId : "");

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
    }

    @GetMapping("/api/schools")
    public ResponseEntity<?> getSchoolResults(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Integer districtId) {

        try {
            // Create a search object with the parameters
            SearchVo searchVo = new SearchVo();
            searchVo.setKeyword(keyword);
            searchVo.setCityId(cityId);
            searchVo.setDistrictId(districtId);

//            List<SchoolInfo> schools = schoolService.searchSchools(searchVo);

            return ResponseEntity.ok("schools");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search schools: " + e.getMessage());
        }
    }

}
