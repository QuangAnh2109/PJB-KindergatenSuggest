package fa.appcode.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "request", schema = "instance_kintergarden_db")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
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

    @Column(name = "request_phone", nullable = false, columnDefinition = "CHAR(12)")
    private String requestPhone;

    @Lob
    @Column(name = "inquiries", columnDefinition = "TEXT")
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

    @Column(name = "update_id", length = 50)
    private String updateId;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "delete_flg", nullable = false)
    private Boolean deleteFlg = false;

    public Request() {
    }

    public Request(AccountInfo account, SchoolInfo school, String fullName, String requestEmail, String requestPhone, String inquiries, Integer requestMasterId, Integer recordNo, String createId, Instant createTime) {
        this.account = account;
        this.school = school;
        this.fullName = fullName;
        this.requestEmail = requestEmail;
        this.requestPhone = requestPhone;
        this.inquiries = inquiries;
        this.requestMasterId = requestMasterId;
        this.recordNo = recordNo;
        this.createId = createId;
        this.createTime = createTime;
        this.updateId = "PARENT";
        this.updateTime = Instant.now();
    }
}