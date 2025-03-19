package fa.appcode.web.controller;

import fa.appcode.services.SchoolInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/search")
@AllArgsConstructor
public class SearchRestController {

    private final SchoolInfoService schoolService;

    @GetMapping("/api/schools")
    public ResponseEntity<?> getSchoolResults(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Integer districtId) {
        return ResponseEntity.ok("");
    }

}
