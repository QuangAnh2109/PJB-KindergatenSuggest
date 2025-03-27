package fa.appcode.common.vo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MySchoolVo {

    @NotNull
    private Integer schoolId;
    @NotNull
    private String schoolName;
    @NotNull
    private String schoolEmail;
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
    private Double avgRating;
    @NotNull
    private Integer totalRating;

    private String schoolIntroduction;
    private String schoolPhone;
    private Double yourRating;
    private LocalDate enrollDate;
    private LocalDate enrollEndDate;
    private List<String> facilities;


}
