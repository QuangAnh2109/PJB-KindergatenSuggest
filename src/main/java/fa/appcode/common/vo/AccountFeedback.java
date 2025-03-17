package fa.appcode.common.vo;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
public class AccountFeedback {
    private final String username;
    private final Instant feedbackTime;
    private final int avgRating;
    private final String feedbackMessage;

    public AccountFeedback(String username, Instant feedbackTime, float avgRating, String feedbackMessage) {
        this.username = username;
        this.feedbackTime = feedbackTime;
        this.avgRating = Math.round(avgRating * 2) * 5;
        this.feedbackMessage = feedbackMessage;
    }
}
