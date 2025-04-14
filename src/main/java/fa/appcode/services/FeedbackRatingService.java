package fa.appcode.services;

import fa.appcode.common.vo.FeedbackRatingRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;

public interface FeedbackRatingService {
    void setSchoolRatingFeedbackData(Model model, FeedbackRatingRequest feedbackRatingRequest) throws DataAccessException;
    void setSchoolRatingData(Model model, FeedbackRatingRequest feedbackRatingRequest) throws DataAccessException;
    void setSchoolFeedbackData(Model model, FeedbackRatingRequest feedbackRatingRequest) throws DataAccessException;
}
