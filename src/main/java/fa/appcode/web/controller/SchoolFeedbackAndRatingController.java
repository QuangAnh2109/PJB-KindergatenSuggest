package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/public")
public class SchoolFeedbackAndRatingController {
    @GetMapping("/school-list/detail/feeddback-rating")
    public String getSchoolFeedbackRating(){
        return Constant.SCHOOL_FEEDBACK_RATING_MANAGER_PAGE;
    }
}
