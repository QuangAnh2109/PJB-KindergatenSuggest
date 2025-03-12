package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolFormManager {
    private int id;

    private String name;

    private int typeId;

    private String address;

    private int cityId;

    private String cityName;

    private int districtId;

    private String districtName;

    private int wardId;

    private String wardName;

    private String email;

    private String phone;

    private int childReceivingAgeId;

    private int educationMethodId;

    private BigDecimal feeTo;

    private BigDecimal feeFrom;

    private String introduction;

    private Instant updateTime;

    private String updateId;

    private int recordNo;

    private boolean deleteFlg;

    private String schoolOwnerEmail;

    private int statusId;
}
