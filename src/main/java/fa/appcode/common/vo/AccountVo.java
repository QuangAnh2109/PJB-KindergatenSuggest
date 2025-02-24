package fa.appcode.common.vo;


import lombok.Getter;
import lombok.Setter;


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

}
