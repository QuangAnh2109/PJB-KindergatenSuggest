package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SortOption;
import fa.appcode.common.vo.*;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.*;
import fa.appcode.services.*;
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
import java.security.Principal;
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
    private final FeedbackService feedbackService;
    private final SchoolInfoService schoolInfoService;
    private final AccountService accountService;

    @GetMapping("/search-result")
    public ResponseEntity<?> searchResult(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Integer districtId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "BY_RATING") SortOption sortBy,
            @RequestParam(required = false) Integer typeSchool,
            @RequestParam(required = false) Integer ageRange,
            @RequestParam(defaultValue = "1") BigDecimal fee_from,
            @RequestParam(defaultValue = "20") BigDecimal fee_to,
            @RequestParam(required = false) List<String> facilities,
            @RequestParam(required = false) List<String> utilities) {

//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy.getSortField()).ascending());
//
//        Page<MySchoolVo> pageResult = schoolInfoService.searchSchoolInfoByAllCategories(
//                keyword, cityId, districtId, pageable, typeSchool, ageRange, fee_from, fee_to, facilities, utilities);

//        Map<Integer, List<String>> listFacilitiesOfSchool = enrollSchoolService.getFacilitiesMapForSchools(pageResult);
//
//        pageResult.getContent().forEach(school -> {
//            school.setFacilities(listFacilitiesOfSchool.getOrDefault(school.getSchoolId(), List.of()));
//        });
//
//        PageVo<MySchoolVo> pageVo = new PageVo<>();
//        pageVo.setContent(pageResult.getContent());
//        pageVo.setTotalPages(pageResult.getTotalPages());
//        pageVo.setTotalElements(pageResult.getTotalElements());
//        pageVo.setCurrentPage(page);
//
        return ResponseEntity.ok("pageVo");
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
    public ResponseEntity<?> createFeedback(
            @SessionAttribute(name = "idAccount", required = true) Integer accountId,
            @RequestBody FeedbackVo feedbackVo) {
        try {
            // Validate input
            Map<String, String> errors = new HashMap<>();

            if (feedbackVo.getSchoolId() == null) {
                errors.put("schoolId", "School ID is required");
            }

            // Check if the feedback message is provided and within length limit
            if (feedbackVo.getFeedbackMessage() == null || feedbackVo.getFeedbackMessage().trim().isEmpty()) {
                errors.put("feedbackMessage", "Feedback message is required");
            } else if (feedbackVo.getFeedbackMessage().trim().length() > 4000) {
                errors.put("feedbackMessage", "Feedback message must be less than 4000 characters");
            }

            // Validate rating values (should be between 0.5 and 5, with 0.5 increments)
            validateRating(errors, feedbackVo.getLearningProgram(), "learningProgram");
            validateRating(errors, feedbackVo.getFacilitiesUtilities(), "facilitiesUtilities");
            validateRating(errors, feedbackVo.getExtracurricularActivities(), "extracurricularActivities");
            validateRating(errors, feedbackVo.getTeacherStaff(), "teacherStaff");
            validateRating(errors, feedbackVo.getHygieneNutrition(), "hygieneNutrition");

            // Check if the user is enrolled in the school
            boolean isEnrolled = enrollSchoolService.isEnrolled(accountId, feedbackVo.getSchoolId());
            if (!isEnrolled) {
                errors.put("schoolId", "You can only rate schools you are currently enrolled in");
            }

            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(errors);
            }

            // Get account and school info
            AccountInfo accountInfo = accountService.getAccountInfoById(accountId);
            SchoolInfo schoolInfo = schoolInfoService.getSchoolInfoById(feedbackVo.getSchoolId());

            if (accountInfo == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Account not found"));
            }

            if (schoolInfo == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "School not found"));
            }

            // Create Feedback ID (composite key)
            FeedbackId feedbackId = new FeedbackId();
            feedbackId.setAccountId(accountId);
            feedbackId.setSchoolId(feedbackVo.getSchoolId());
            feedbackId.setFeedbackTime(Instant.now());

            // Create Feedback entity
            Feedback feedback = new Feedback();
            feedback.setId(feedbackId);
            feedback.setAccountInfo(accountInfo);
            feedback.setSchool(schoolInfo);
            feedback.setLearningProgram(feedbackVo.getLearningProgram());
            feedback.setFacilitiesUtilities(feedbackVo.getFacilitiesUtilities());
            feedback.setExtracurricularActivities(feedbackVo.getExtracurricularActivities());
            feedback.setTeacherStaff(feedbackVo.getTeacherStaff());
            feedback.setHygieneNutrition(feedbackVo.getHygieneNutrition());
            feedback.setFeedbackMessage(feedbackVo.getFeedbackMessage());

            // Set audit fields
            feedback.setRecordNo(1);
            feedback.setCreateId("PARENT");
            feedback.setCreateTime(Instant.now());
            feedback.setUpdateId("PARENT");
            feedback.setUpdateTime(Instant.now());
            feedback.setDeleteFlg(false);

            // Save feedback
            feedbackService.createFeedback(feedback);

            return ResponseEntity.ok("Feedback created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create feedback: " + e.getMessage()));
        }
    }

    // Helper method to validate ratings
    private void validateRating(Map<String, String> errors, double rating, String fieldName) {
        // Check if the rating is between 0.5 and 5
        if (rating < 0.5 || rating > 5) {
            errors.put(fieldName, "Rating must be between 0.5 and 5");
        }

        // Check if the rating is in 0.5 increments
        double fractionalPart = rating % 1;
        if (fractionalPart != 0 && fractionalPart != 0.5) {
            errors.put(fieldName, "Rating must be in 0.5 increments");
        }
    }
    
    @GetMapping("/filter-feedback")
    public ResponseEntity<List<FeedbackListVo>> filterFeedback(
            @RequestParam Integer schoolId,
            @RequestParam(defaultValue = "0") String filter) {
        
        double minRating = 0.0;
        double maxRating = 5.0;
        boolean filterByRating = true;
        
        switch (filter) {
            case "5":
                minRating = 5.0;
                maxRating = 5.0;
                break;
            case "4":
                minRating = 4.0;
                maxRating = 5.0;
                break;
            case "3":
                minRating = 3.0;
                maxRating = 4.0;
                break;
            case "2":
                minRating = 2.0;
                maxRating = 3.0;
                break;
            case "1":
                minRating = 1.0;
                maxRating = 2.0;
                break;
            case "0":
            default:
                // "0" or any other value, return all feedback without filtering by rating
                filterByRating = false;
                break;
        }
        
        List<FeedbackListVo> filteredFeedback;
        if (filterByRating) {
            filteredFeedback = feedbackService.findListFeedbackBySchoolIdAndRating(schoolId, minRating, maxRating);
        } else {
            filteredFeedback = feedbackService.findListFeedbackBySchoolId(schoolId);
        }
        
        return ResponseEntity.ok(filteredFeedback);
    }


    /**
     * Create a new counseling request.
     *
     * @param fullName  the full name of the requester
     * @param email     the email of the requester
     * @param phone     the phone number of the requester
     * @param inquiries the inquiries or questions from the requester
     * @param principal the currently logged-in user
     */
    @PostMapping("/createRequest")
    public ResponseEntity<?> createRequestCounseling(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String inquiries,
            @RequestParam Integer schoolId,
            Principal principal) {

        Map<String, String> errors = new HashMap<>();

        // Enhanced server-side validation
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.put("fullName", globalConfig.getFullNameRequired());
        } else if (fullName.trim().length() > 255) {
            errors.put("fullName", globalConfig.getFullNameLength());
        }

        if (email == null || email.trim().isEmpty()) {
            errors.put("email", globalConfig.getEmailRequired());
        } else if (!email.matches("[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            errors.put("email", globalConfig.getEmailFormat());
        } else if (email.length() > 255) {
            errors.put("email", globalConfig.getEmailLength());
        }

        if (phone == null || phone.trim().isEmpty()) {
            errors.put("phone", "Phone number is required");
        } else {
            // First check if the input contains only digits
            if (!phone.matches("^[0-9]+$")) {
                errors.put("phone", globalConfig.getPhoneFormat());
            }
            // Then check if it's exactly 10 digits
            else if (!phone.matches("^[0-9]{10}$")) {
                errors.put("phone", globalConfig.getPhoneLength());
            }
        }

        if (inquiries == null || inquiries.trim().isEmpty()) {
            errors.put("inquiries", globalConfig.getInquiriesRequired());
        } else if (inquiries.trim().length() > 4000) {
            errors.put("inquiries", globalConfig.getInquiriesLength());
        }

        // Validate school ID
        if (schoolId == null) {
            errors.put("schoolId", "School ID is required");
        } else {
            try {
                // Check if the school exists
                SchoolInfo school = schoolInfoService.getSchoolInfoById(schoolId);
                if (school == null) {
                    errors.put("schoolId", "Invalid school selected");
                }
            } catch (Exception e) {
                errors.put("schoolId", "Error validating school: " + e.getMessage());
            }
        }

        // Return validation errors if any
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            // Get the logged-in user
            AccountInfo accountInfo = accountService.getAccountInfo(principal);

            // Get the school info
            SchoolInfo school = schoolInfoService.getSchoolInfoById(schoolId);

            // Create the request
            Request request = new Request(
                    accountInfo,
                    school,
                    fullName,
                    email,
                    phone,
                    inquiries,
                    1,  // Status
                    1,  // Priority
                    "PARENT",  // Role
                    Instant.now()  // Create time
            );

            // Save the request
            requestService.createRequest(request);

            return ResponseEntity.ok(globalConfig.getRequestSuccess());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", globalConfig.getRequestFailed() + e.getMessage())
            );
        }
    }
}
