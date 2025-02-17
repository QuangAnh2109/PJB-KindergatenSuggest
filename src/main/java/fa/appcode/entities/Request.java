package fa.appcode.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Accountinfo account;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Accountinfo getAccount() {
        return account;
    }

    public void setAccount(Accountinfo account) {
        this.account = account;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRequestEmail() {
        return requestEmail;
    }

    public void setRequestEmail(String requestEmail) {
        this.requestEmail = requestEmail;
    }

    public String getRequestPhone() {
        return requestPhone;
    }

    public void setRequestPhone(String requestPhone) {
        this.requestPhone = requestPhone;
    }

    public String getInquiries() {
        return inquiries;
    }

    public void setInquiries(String inquiries) {
        this.inquiries = inquiries;
    }

    public Integer getRequestMasterId() {
        return requestMasterId;
    }

    public void setRequestMasterId(Integer requestMasterId) {
        this.requestMasterId = requestMasterId;
    }

    public Integer getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(Integer recordNo) {
        this.recordNo = recordNo;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Boolean deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

}