package fa.appcode.common.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class MyRequestVo {

        @NotNull
        private Integer id;

        @NotNull
        private String fullName;

        @NotNull
        private String requestEmail;

        @NotNull
        private String requestPhone;

        @NotNull
        private String requestSchoolName;

        @NotNull
        private String address;

        @NotNull
        private String inquires;

        @NotNull
        private String requestMasterName;

        @NotNull
        private Instant createTime;

        @NotNull
        private String emailSchool;

        @NotNull
        private Double avgRating;

        @NotNull
        private Integer totalRating;

        @NotNull
        private Double feeFrom;

        @NotNull
        private String ageRange;

        @NotNull
        private int schoolId;
    }

