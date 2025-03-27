package fa.appcode.common.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class FeedbackVo {
    @NotNull
    private Integer schoolId;
    @NotNull
    private Integer accountId;
    @NotNull
    private Double learningProgramRating;
    @NotNull
    private Double facultyAndUtilityRating;
    @NotNull
    private Double extracurricularRating;
    @NotNull
    private Double teacherRating;
    @NotNull
    private Double hygieneRating;
    @NotNull
    private String feedback;

}
