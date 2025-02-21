package fa.appcode.common.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

    @NoArgsConstructor
    @Data
    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    public class AccountVo {

        private String fullName;

        @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Must be a well-formed email address.Ex: example@gmail.com.")
        private String email;
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{12,25}$",
                message = "Password must be at least 12 and max 25 length containing at least 1 uppercase, 1 lowercase, 1 special character and 1 digit.")
        private String password;

        @NotBlank(message = "Display name cannot be empty.")
        private String displayName;

        @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number must be 10 digits.")
        @NotBlank(message = "Phone number cannot be empty.")
        private String phoneNumber;
        private Boolean isActive = true;

    }


