package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SchoolRatingFeedback {
    private final int schoolId;
    private final long avgRating;
    private final long avgLearningProgram;
    private final long avgFacilitiesUtilities;
    private final long avgExtracurricularActivities;
    private final long avgTeacherStaff;
    private final long avgHygieneNutrition;
    private final long totalFeedbacks;

    public SchoolRatingFeedback(int schoolId, Double avgRating, Double avgLearningProgram, Double avgFacilitiesUtilities, Double avgExtracurricularActivities, Double avgTeacherStaff, Double avgHygieneNutrition, long totalFeedbacks) {
        this.schoolId = schoolId;
        this.avgRating = Math.round(avgRating * 2) * 5;
        this.avgLearningProgram = Math.round(avgLearningProgram * 2) * 5;
        this.avgFacilitiesUtilities = Math.round(avgFacilitiesUtilities * 2) * 5;
        this.avgExtracurricularActivities = Math.round(avgExtracurricularActivities * 2) * 5;
        this.avgTeacherStaff = Math.round(avgTeacherStaff * 2) * 5;
        this.avgHygieneNutrition = Math.round(avgHygieneNutrition * 2) * 5;
        this.totalFeedbacks = totalFeedbacks;
    }
}