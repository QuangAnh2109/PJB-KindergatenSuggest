package fa.appcode.common.vo;

import fa.appcode.entities.SchoolFacility;
import fa.appcode.entities.SchoolUtility;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolSearchVo {
    @NotNull
    private int schoolId;

    @NotNull
    private String schoolName;

    @NotNull
    @Email
    private String schoolEmail;

    @NotNull
    private Double feeFrom;

    @NotNull
    private String ageRange;

    @NotNull
    private String schoolType;

    @NotNull
    private String schoolStatus;

    @NotNull
    private String schoolAddress;

    @NotNull
    private String schoolImage;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "5.0", inclusive = true)
    private Double avgRating;

    @NotNull
    private Integer totalRating;

    @NotNull
    private List<SchoolUtility> utilities;

    @NotNull
    private List<SchoolFacility> facilities;


}
