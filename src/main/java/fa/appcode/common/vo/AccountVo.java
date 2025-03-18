    package fa.appcode.common.vo;


    import lombok.AllArgsConstructor;
    import lombok.Data;

    import lombok.NoArgsConstructor;

    import java.time.LocalDate;


    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public class AccountVo {
        private Integer id;
        private String imageUrl;
        private String dob;
        private String fullAddress;
        private String role;
        private String status;
        private Integer recordNo;
        private String datetimeChangePass;
        private String fullName;
        private String email;
        private String phone;
        private String password;
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
