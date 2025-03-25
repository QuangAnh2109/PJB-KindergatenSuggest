package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SortOption;
import fa.appcode.common.vo.FeedbackVo;
import fa.appcode.common.vo.MyRequestVo;
import fa.appcode.common.vo.MySchoolVo;
import fa.appcode.common.vo.PageVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.Feedback;
import fa.appcode.services.EnrollSchoolService;
import fa.appcode.services.FeedbackService;
import fa.appcode.services.RequestService;
import fa.appcode.services.impl.EnrollSchoolServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@AllArgsConstructor
@RestController
public class RestControllerAjax {

    private final RequestService requestService;
    private final EnrollSchoolService enrollSchoolService;
    private final EnrollSchoolServiceImpl enrollSchoolServiceImpl;
    private final GlobalConfig globalConfig;


    @GetMapping("/search-result")
    public String searchResult(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Integer cityId,
                               @RequestParam(required = false) Integer districtId,
                               Model model,
                               @RequestParam(defaultValue = Constant.INIT_PAGE) int page,
                               @RequestParam(defaultValue = Constant.PAGE_SIZE) int size,
                               @RequestParam(defaultValue = "BY_RATING", required = false) SortOption sortBy,
                               @RequestParam Integer typeSchool,
                               @RequestParam Integer ageRange,
                               @RequestParam BigDecimal fee_from,
                               @RequestParam BigDecimal fee_to) {

        return "Search result";
    }

    @GetMapping("/my-request")
    public ResponseEntity<PageVo<MyRequestVo>> myRequest(
            @SessionAttribute(name = "idAccount", required = true) Integer accountId,
            @RequestParam(defaultValue = Constant.INIT_PAGE) int page,
            @RequestParam(defaultValue = Constant.PAGE_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<MyRequestVo> requestPage = requestService.findRequestByAccountId(accountId, pageable);

        PageVo<MyRequestVo> pageDto = new PageVo<>();
        pageDto.setContent(requestPage.getContent());
        pageDto.setTotalPages(requestPage.getTotalPages());
        pageDto.setTotalElements(requestPage.getTotalElements());
        pageDto.setCurrentPage(page);

        return ResponseEntity.ok(pageDto);
    }

    @GetMapping("/api/current-schools")
    public ResponseEntity<PageVo<MySchoolVo>> getCurrentSchools(
            @SessionAttribute(name = "idAccount", required = true) Integer id,
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, globalConfig.getSizeOfPage());
        Page<MySchoolVo> pageResult = enrollSchoolService.findListSchoolParentEnrolledByParentId(id, pageable);
        Map<Integer,List<String>> listFacilitiesOfCurrentSchool = enrollSchoolServiceImpl.getFacilitiesMapForSchools(pageResult);

        // Add facilities to each school object for easier access in frontend
        pageResult.getContent().forEach(school -> {
            school.setFacilities(listFacilitiesOfCurrentSchool.getOrDefault(school.getSchoolId(), List.of()));
        });

        PageVo<MySchoolVo> pageVo = new PageVo<>();
        pageVo.setContent(pageResult.getContent());
        pageVo.setTotalPages(pageResult.getTotalPages());
        pageVo.setTotalElements(pageResult.getTotalElements());
        pageVo.setCurrentPage(page);
        return ResponseEntity.ok(pageVo);
    }

    @GetMapping("/api/previous-schools")
    public ResponseEntity<PageVo<MySchoolVo>> getPreviousSchools(
            @SessionAttribute(name = "idAccount", required = true) Integer id,
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, globalConfig.getSizeOfPage());
        Page<MySchoolVo> pageResult = enrollSchoolService.findListSchoolParentPreEnrolledByParentId(id, pageable);
        Map<Integer,List<String>> listFacilitiesOfPreSchool = enrollSchoolServiceImpl.getFacilitiesMapForSchools(pageResult);

//         Add facilities to each school object for easier access in frontend
        pageResult.getContent().forEach(school -> {
            school.setFacilities(listFacilitiesOfPreSchool.getOrDefault(school.getSchoolId(), List.of()));
        });

        PageVo<MySchoolVo> pageVo = new PageVo<>();
        pageVo.setContent(pageResult.getContent());
        pageVo.setTotalPages(pageResult.getTotalPages());
        pageVo.setTotalElements(pageResult.getTotalElements());
        pageVo.setCurrentPage(page);
        return ResponseEntity.ok(pageVo);
    }




    @PostMapping("/create-feedback")
    public ResponseEntity<String> createFeedback(
            @SessionAttribute(name = "idAccount", required = true) Integer accountId,
            @RequestBody FeedbackVo feedbackVo) {
        // Implement the feedback creation logic
        try {
            return ResponseEntity.ok("Feedback created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create feedback: " + e.getMessage());
        }
    }

    @GetMapping("/feedback-list")
    public ResponseEntity<String> getFeedbackList(){
        return ResponseEntity.ok("Feedback list");
    }
}
