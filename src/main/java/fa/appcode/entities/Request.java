package fa.appcode.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "request", schema = "instance_kintergarden_db")
public class Request {
    @Id
    @Column(name = "request_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountInfo account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolInfo school;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "request_email", nullable = false)
    private String requestEmail;

    @Column(name = "request_phone", nullable = false, length = 12)
    private String requestPhone;

    @Lob
    @Column(name = "inquiries")
    private String inquiries;

    @Column(name = "request_master_id", nullable = false)
    private Integer requestMasterId;

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