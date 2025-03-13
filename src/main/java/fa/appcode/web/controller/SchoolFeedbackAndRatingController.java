package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.services.AccountService;
import fa.appcode.services.FeedbackRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class SchoolFeedbackAndRatingController {

    private final FeedbackRatingService feedbackRatingService;

    private final AccountService accountService;

    @GetMapping("admin/school-list/detail/feedback-rating/{schoolId}&{accountId}")
    public String getSchoolFeedbackRatingByAdmin(Model model, @PathVariable("schoolId") int schoolId, @PathVariable("accountId") int accountId){
        feedbackRatingService.setBaseData(model, schoolId, accountId);
        return Constant.SCHOOL_FEEDBACK_RATING_MANAGER_PAGE;
    }

    @GetMapping("school-owner/school-list/detail/feedback-rating/{schoolId}")
    public String getSchoolFeedbackRatingByManager(Model model, @PathVariable("schoolId") int schoolId){
        System.out.println("run");
        feedbackRatingService.setBaseData(model, schoolId, accountService.getAccountIdByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        System.out.println("run");
        return Constant.SCHOOL_FEEDBACK_RATING_MANAGER_PAGE;
    }

    @PostMapping("/school-list/school-rating/time")
    @ResponseBody
    public String getRatingByTime(@RequestBody Map<String, String> request){
        return "sad";
    }
}
