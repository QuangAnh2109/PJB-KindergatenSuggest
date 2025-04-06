package fa.appcode.common.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Data
public class FeedbackVo {
    private Integer schoolId;
    private Float learningProgram;
    private Float facilitiesUtilities;
    private Float extracurricularActivities;
    private Float teacherStaff;
    private Float hygieneNutrition;
    private String feedbackMessage;
}
