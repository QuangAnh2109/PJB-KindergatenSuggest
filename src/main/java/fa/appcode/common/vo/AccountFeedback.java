package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class AccountFeedback {
    private String username;
    private Instant feedbackTime;
    private float learningProgram;
    private float facilitiesAndUtilities;
    private float extracurricularActivities;
    private float teachersAndStaff;
    private float hygieneAndNutrition;
    private String feedbackMessage;
}
