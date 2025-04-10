package fa.appcode.web.controller;

import fa.appcode.common.utils.AuthenticationGet;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.FeedbackRatingRequest;
import fa.appcode.exceptions.FromToDateException;
import fa.appcode.services.EntityValidateService;
import fa.appcode.services.FeedbackRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class SchoolFeedbackAndRatingController {

    private final FeedbackRatingService feedbackRatingService;

    private final EntityValidateService entityValidateService;

    @GetMapping("manager/school/feedback-rating/{schoolId}")
    public String getSchoolFeedbackRatingByManager(@PathVariable("schoolId") int schoolId, Model model){
        // Setup form
        model.addAttribute("schoolId", schoolId);

        // Get all feedback and rating info
        feedbackRatingService.setSchoolRatingFeedbackData(model, new FeedbackRatingRequest(schoolId));
        return Constant.SCHOOL_FEEDBACK_RATING_MANAGER_PAGE;
    }

    @PostMapping("/manager/school/rating/search/")
    public String searchSchoolRating(@RequestBody FeedbackRatingRequest feedbackRatingRequest, Model model) throws FromToDateException {
        entityValidateService.validateRatingFeedbackDateFromTo(feedbackRatingRequest.getFromDate(), feedbackRatingRequest.getToDate());

        // Setup form
        feedbackRatingRequest.setAccountEmail(AuthenticationGet.getAccountEmailByAuthen());
        model.addAttribute("schoolId", feedbackRatingRequest.getSchoolId());

        // Update all feedback and rating info by time from, to
        feedbackRatingService.setSchoolRatingFeedbackData(model, feedbackRatingRequest);
        return Constant.SCHOOL_FEEDBACK_RATING_MANAGER_PAGE + " :: main-contain";
    }

    @PostMapping("/manager/school/feedback/search/")
    public String searchSchoolFeedback(@RequestBody FeedbackRatingRequest feedbackRatingRequest, Model model) throws FromToDateException {
        entityValidateService.validateRatingFeedbackDateFromTo(feedbackRatingRequest.getFromDate(), feedbackRatingRequest.getToDate());

        // Setup form
        feedbackRatingRequest.setAccountEmail(AuthenticationGet.getAccountEmailByAuthen());
        model.addAttribute("schoolId", feedbackRatingRequest.getSchoolId());

        // Set all feedback info by avg star
        feedbackRatingService.setSchoolFeedbackData(model, feedbackRatingRequest);
        return Constant.SCHOOL_FEEDBACK_RATING_MANAGER_PAGE + " :: feedback-list";
    }
}
