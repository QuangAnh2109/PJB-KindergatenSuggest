package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchoolRatingFeedbackManager {
    private float averageRating;
    private int totalFeedback;
    private float learningProgram;
    private float facilitiesAndUtilities;
    private float extracurricularActivities;
    private float teachersAndStaff;
    private float hygieneAndNutrition;
}