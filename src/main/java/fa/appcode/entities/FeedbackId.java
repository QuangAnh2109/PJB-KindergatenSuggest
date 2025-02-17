package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.time.Instant;
import java.util.Objects;

@Embeddable
public class FeedbackId implements java.io.Serializable {
    private static final long serialVersionUID = 4261769430719373111L;
    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "school_id", nullable = false)
    private Integer schoolId;

    @Column(name = "feedback_time", nullable = false)
    private Instant feedbackTime;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Instant getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Instant feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FeedbackId entity = (FeedbackId) o;
        return Objects.equals(this.feedbackTime, entity.feedbackTime) &&
                Objects.equals(this.accountId, entity.accountId) &&
                Objects.equals(this.schoolId, entity.schoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackTime, accountId, schoolId);
    }

}