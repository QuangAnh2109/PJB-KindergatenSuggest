package fa.appcode.services.impl;

import fa.appcode.common.vo.FeedbackListVo;
import fa.appcode.repositories.FeedbackRepository;
import lombok.AllArgsConstructor;
import fa.appcode.common.vo.RatingVo;
import fa.appcode.entities.Feedback;
import fa.appcode.services.FeedbackService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    public void createFeedback(Feedback feedback) {

    }

    @Override
    public RatingVo findRatingBySchoolId(Integer schoolId) {
        return feedbackRepository.findRatingBySchoolId(schoolId);
    }

    @Override
    public List<FeedbackListVo> findListFeedbackBySchoolId(Integer schoolId) throws DataAccessException {
        return feedbackRepository.findListFeedbackBySchoolId(schoolId);
    }


}
