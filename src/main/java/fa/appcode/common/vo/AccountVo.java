package fa.appcode.common.vo;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class AccountVo {

    private Integer id;
    private String fullName;
    private String email;
    private String imageUrl;
    private String phone;
    private String dob;
    private String fullAddress;
    private String role;
    private Integer roleId;
    private String status;

    public AccountVo() {
    }

    public AccountVo(Integer id, String fullName, String email, String phone, LocalDate dob, String fullAddress, String role, String status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dob = (dob != null) ? dob.toString() : null;
        this.fullAddress = fullAddress;
        this.role = role;
        this.status = status;
    }
}
