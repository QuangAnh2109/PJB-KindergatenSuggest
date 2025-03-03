package fa.appcode.web.controller;

import fa.appcode.common.vo.SearchVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@ResponseBody
public class SearchRestController {
    @GetMapping("/public/search/process")
    public ResponseEntity<Void> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Integer districtId) {

        String redirectUrl = "/public/search/results?keyword=" + (keyword != null ? keyword : "") +
                (cityId != null ? "&cityId=" + cityId : "") +
                (districtId != null ? "&districtId=" + districtId : "");

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
    }

}
