package fa.appcode.common.vo;

import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.City;
import fa.appcode.entities.District;
import fa.appcode.entities.Ward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
@Setter
public class SchoolInfoVo {
    private Integer id;

    private AccountInfo account;

    private String schoolName;

    private String schoolEmail;

    private String imageUrl;

    private String schoolPhone;

    private BigDecimal feeFrom;

    private BigDecimal feeTo;

    private String schoolAddress;

    private Ward ward;

    private District district;

    private City city;

    private String schoolIntroduction;

    private Instant postedDate;

    private Integer childReceivingAgeId;

    private Integer educationMethodId;

    private Integer typeId;

    private Integer statusId;

}
