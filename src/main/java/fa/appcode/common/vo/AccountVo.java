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

import java.time.LocalDate;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Getter
@Setter
public class AccountVo {

    private Integer id;
    private String imageUrl;
    private String dob;
    private String fullAddress;
    private String role;
    private String status;
    private String recordNo;
    private String datetimeChangePass;
    @NotNull
    @NotEmpty(message = "This field is required.")
    private String fullName;
    @NotEmpty(message = "This field is required.")
    @Email(message = "Invalid email format.")
    private String email;
    @NotEmpty(message = "PzThis field is required.")
    @Pattern(regexp = Constant.PHONE_REGEX, message = "Invalid phone number format.")
    private String phone;
    @NotEmpty(message = "This field is required.")
    @Pattern(regexp = Constant.PASSWORD_REGEX, message = "Password must contain at least one uppercase letter, one number, one special character, and be at least 12 characters long.")
    private String password;
    @NotEmpty(message = "This field is required.")
    private String confirmPassword;

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
