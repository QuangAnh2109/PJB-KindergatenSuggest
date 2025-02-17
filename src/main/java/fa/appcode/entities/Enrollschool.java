package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "enrollschool")
public class Enrollschool {
    @EmbeddedId
    private EnrollschoolId id;

    @Column(name = "enroll_date")
    private LocalDate enrollDate;

    @Column(name = "enroll_enddate")
    private LocalDate enrollEnddate;

    @Column(name = "status", nullable = false)
    private Boolean status = false;

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

    public EnrollschoolId getId() {
        return id;
    }

    public void setId(EnrollschoolId id) {
        this.id = id;
    }

    public LocalDate getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(LocalDate enrollDate) {
        this.enrollDate = enrollDate;
    }

    public LocalDate getEnrollEnddate() {
        return enrollEnddate;
    }

    public void setEnrollEnddate(LocalDate enrollEnddate) {
        this.enrollEnddate = enrollEnddate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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