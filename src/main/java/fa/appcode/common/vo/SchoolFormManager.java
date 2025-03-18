package fa.appcode.common.vo;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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

    private String imgageUrl;

    private Instant updateTime;

    private String updateId;

    private int recordNo;

    private boolean deleteFlg;

    private String schoolOwnerEmail;

    private int statusId;

    public SchoolFormManager(boolean deleteFlg, String schoolOwnerEmail, int recordNo, int id, int typeId, String name, String address, int cityId, int districtId, int wardId, String email, String phone, int childReceivingAgeId, int educationMethodId, BigDecimal feeTo, BigDecimal feeFrom, String introduction, String imgageUrl, Instant updateTime, String updateId) {
        this.deleteFlg = deleteFlg;
        this.schoolOwnerEmail = schoolOwnerEmail;
        this.recordNo = recordNo;
        this.id = id;
        this.typeId = typeId;
        this.name = name;
        this.address = address;
        this.cityId = cityId;
        this.districtId = districtId;
        this.wardId = wardId;
        this.email = email;
        this.phone = phone;
        this.childReceivingAgeId = childReceivingAgeId;
        this.educationMethodId = educationMethodId;
        this.feeTo = feeTo;
        this.feeFrom = feeFrom;
        this.introduction = introduction;
        this.imgageUrl = imgageUrl;
        this.updateTime = updateTime;
        this.updateId = updateId;
    }
}
