package fa.appcode.common.vo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor

public class MySchoolVo {

    @NotNull
    private Integer schoolId;
    @NotNull
    private String schoolName;
    @NotNull
    private String schoolAddress;
    @NotNull
    private BigDecimal feeFrom;
    @NotNull
    private String ageRange;
    @NotNull
    private String schoolType;
    @NotNull
    private String schoolImage;
    @NotNull
    private double avgRating;
    @NotNull
    private int totalRating;

}
