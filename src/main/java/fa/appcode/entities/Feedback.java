package fa.appcode.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "feedback", schema = "instance_kintergarden_db")
public class Feedback {
    @EmbeddedId
    private FeedbackId id;

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolInfo school;

    @Column(name = "learning program", nullable = false)
    private Float learningProgram;

    @Column(name = "facilities_utilities", nullable = false)
    private Float facilitiesUtilities;

    @Column(name = "extracurricular_activities", nullable = false)
    private Float extracurricularActivities;

    @Column(name = "teacher_staff", nullable = false)
    private Float teacherStaff;

    @Column(name = "hygiene_nutrition", nullable = false)
    private Float hygieneNutrition;

    @Lob
    @Column(name = "feedback_message", nullable = false, columnDefinition = "TEXT")
    private String feedbackMessage;

    @ColumnDefault("(1)")
    @Column(name = "record_no", nullable = false)
    private Integer recordNo;

    @Column(name = "create_id", nullable = false, length = 50)
    private String createId;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @Column(name = "update_id", nullable = false, length = 50)
    private String updateId;

    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    @Column(name = "delete_flg", nullable = false)
    private Boolean deleteFlg = false;

}