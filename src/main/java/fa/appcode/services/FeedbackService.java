package fa.appcode.services;

import fa.appcode.common.vo.FeedbackListVo;
import fa.appcode.common.vo.RatingVo;
import fa.appcode.entities.Feedback;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackService {
    void createFeedback(Feedback feedback);

    RatingVo findRatingBySchoolId(Integer schoolId);

    List<FeedbackListVo> findListFeedbackBySchoolId(@Param("schoolId") Integer schoolId);
}
