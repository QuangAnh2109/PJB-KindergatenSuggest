package fa.appcode.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "feedback")
public class Feedback {
    @EmbeddedId
    private FeedbackId id;

    @Column(name = "`learning program`", nullable = false)
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
    @Column(name = "feedback_message", nullable = false)
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

    public FeedbackId getId() {
        return id;
    }

    public void setId(FeedbackId id) {
        this.id = id;
    }

    public Float getLearningProgram() {
        return learningProgram;
    }

    public void setLearningProgram(Float learningProgram) {
        this.learningProgram = learningProgram;
    }

    public Float getFacilitiesUtilities() {
        return facilitiesUtilities;
    }

    public void setFacilitiesUtilities(Float facilitiesUtilities) {
        this.facilitiesUtilities = facilitiesUtilities;
    }

    public Float getExtracurricularActivities() {
        return extracurricularActivities;
    }

    public void setExtracurricularActivities(Float extracurricularActivities) {
        this.extracurricularActivities = extracurricularActivities;
    }

    public Float getTeacherStaff() {
        return teacherStaff;
    }

    public void setTeacherStaff(Float teacherStaff) {
        this.teacherStaff = teacherStaff;
    }

    public Float getHygieneNutrition() {
        return hygieneNutrition;
    }

    public void setHygieneNutrition(Float hygieneNutrition) {
        this.hygieneNutrition = hygieneNutrition;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public Integer getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(Integer recordNo) {
        this.recordNo = recordNo;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Boolean deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

}