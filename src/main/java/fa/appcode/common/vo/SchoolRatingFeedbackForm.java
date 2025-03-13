package fa.appcode.common.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

@Getter
@Builder
public class SchoolRatingFeedbackForm {
    private int schoolId;
    private String accountEmail;
    private Instant from;
    private Instant to;
    private boolean one;
    private boolean two;
    private boolean three;
    private boolean four;
    private boolean five;
    private boolean deleteFlg;
}
