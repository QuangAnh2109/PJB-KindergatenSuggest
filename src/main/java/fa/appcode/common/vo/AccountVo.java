package fa.appcode.common.vo;

import fa.appcode.common.constant.Constant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Getter
@Setter
public class AccountVo {
    private Integer id;
    private String dob;
    private String imageUrl;
    private String fullAddress;
    private String role;
    private String status;
    @NotNull
    @NotEmpty(message = "")
    private String fullName;

    @NotEmpty(message = "This field is required.")
    @Email(message = "Email khong hop le")
    private String email;

    @NotEmpty(message = "This field is required.")
    @NotEmpty(message = "This field is required.")
    @Pattern(regexp = Constant.PHONE_REGEX, message = "Số điện thoại không hợp lệ!")
    private String phone;

    @NotEmpty(message = "This field is required.")
    @Pattern(regexp = Constant.PASSWORD_REGEX, message = "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 số và 1 ký tự đặc biệt, tối thiểu 12 ký tự!")
    private String password;
    @NotEmpty(message = "This field is required.")
    private String confirmPassword;



}
