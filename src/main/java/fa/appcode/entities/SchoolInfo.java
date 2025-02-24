package fa.appcode.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "school_info")
public class SchoolInfo {
    @Id
    @Column(name = "school_id", nullable = false)
    private Integer id;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "school_email", nullable = false)
    private String schoolEmail;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "school_phone", nullable = false, columnDefinition = "CHAR(12)")
    private String schoolPhone;

    @Column(name = "fee_from", nullable = false, precision = 15, scale = 5)
    private BigDecimal feeFrom;

    @Column(name = "fee_to", nullable = false, precision = 15, scale = 5)
    private BigDecimal feeTo;

    @Column(name = "school_address", nullable = false)
    private String schoolAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Lob
    @Column(name = "school_introduction", columnDefinition = "TEXT")
    private String schoolIntroduction;

    @Column(name = "posted_date")
    private Instant postedDate;

    @Column(name = "child_receiving_age_id", nullable = false)
    private Integer childReceivingAgeId;

    @Column(name = "education_method_id", nullable = false)
    private Integer educationMethodId;

    @Column(name = "type_id", nullable = false)
    private Integer typeId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @ColumnDefault("(1)")
    @Column(name = "record_no", nullable = false)
    private Integer recordNo;

    @Column(name = "create_id", nullable = false, length = 50)
    private String createId;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @Column(name = "update_id", nullable = false, length = 50)
    private String updateId;

    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    @Column(name = "delete_flg", nullable = false)
    private Boolean deleteFlg = false;

}