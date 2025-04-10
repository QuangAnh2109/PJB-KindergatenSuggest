package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.FeedbackRatingRequest;
import fa.appcode.common.vo.SchoolRatingFeedback;
import fa.appcode.config.GlobalConfig;
import fa.appcode.exceptions.FromToDateException;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.FeedbackRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FeedbackRatingServiceImpl implements FeedbackRatingService {

    private final SchoolInfoRepository schoolInfoRepository;

    private final GlobalConfig globalConfig;

    @Override
    public void setSchoolRatingFeedbackData(Model model, FeedbackRatingRequest feedbackRatingRequest) throws DataAccessException {
        setSchoolRatingData(model, feedbackRatingRequest);
        setSchoolFeedbackData(model, feedbackRatingRequest);
    }

    @Override
    public void setSchoolRatingData(Model model, FeedbackRatingRequest feedbackRatingRequest) throws DataAccessException {
        // Get the rating for the school
        SchoolRatingFeedback schoolRatingFeedback = schoolInfoRepository.getSchoolRatingFeedbackBySchoolId(feedbackRatingRequest);
        model.addAttribute("rating", (schoolRatingFeedback == null)?new SchoolRatingFeedback(feedbackRatingRequest.getSchoolId()) : schoolRatingFeedback);
    }

    @Override
    public void setSchoolFeedbackData(Model model, FeedbackRatingRequest feedbackRatingRequest) throws DataAccessException {
        // Get the feedback list for the school
        model.addAttribute("feedbackList", schoolInfoRepository.getAllAccountFeedbackBySchoolId(feedbackRatingRequest, PageRequest.of(feedbackRatingRequest.getPageNumber(), globalConfig.getSizeOfPageSchoolRatingFeedback())));
    }
}
