package fa.appcode.entities;

import fa.appcode.entities.City;
import fa.appcode.entities.District;
import fa.appcode.entities.Ward;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "account_info", schema = "instance_kintergarden_db")
public class AccountInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 100)
    @NotNull
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Size(max = 12)
    @Column(name = "phone", length = 12)
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @NotNull
    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @NotNull
    @ColumnDefault("(1)")
    @Column(name = "record_no", nullable = false)
    private Integer recordNo;

    @Size(max = 50)
    @NotNull
    @Column(name = "create_id", nullable = false, length = 50)
    private String createId;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @Size(max = 50)
    @NotNull
    @Column(name = "update_id", nullable = false, length = 50)
    private String updateId;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    @NotNull
    @Column(name = "delete_flg", nullable = false)
    private Boolean deleteFlg = false;

}
