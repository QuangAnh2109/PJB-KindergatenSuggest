package fa.appcode.common.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class FeedbackListVo {
    @NotNull
    private Integer accountId;
    @NotNull
    private String accountName;
    private String accountImage;

    private Instant feedbackTime;
    private Float learningProgram;
    private Float facilitiesUtilities;
    private Float extracurricularActivities;
    private Float teacherStaff;
    private Float hygieneNutrition;

    private Double avgRating;
    @NotNull
    private String feedback;

}
