package fa.appcode.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "schoolinfo")
public class Schoolinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id", nullable = false)
    private Integer id;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "school_email", nullable = false)
    private String schoolEmail;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "school_phone", nullable = false, length = 12)
    private String schoolPhone;

    @Column(name = "fee_from", nullable = false, precision = 15, scale = 5)
    private BigDecimal feeFrom;

    @Column(name = "fee_to", nullable = false, precision = 15, scale = 5)
    private BigDecimal feeTo;

    @Column(name = "school_address", nullable = false)
    private String schoolAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Lob
    @Column(name = "school_introduction")
    private String schoolIntroduction;

    @Column(name = "posted_date", nullable = false)
    private LocalDate postedDate;

    @Column(name = "child_receiving_age_master_id", nullable = false)
    private Integer childReceivingAgeMasterId;

    @Column(name = "education_method_master_id", nullable = false)
    private Integer educationMethodMasterId;

    @Column(name = "school_type_master_id", nullable = false)
    private Integer schoolTypeMasterId;

    @Column(name = "school_status_master_id", nullable = false)
    private Integer schoolStatusMasterId;

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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSchoolPhone() {
        return schoolPhone;
    }

    public void setSchoolPhone(String schoolPhone) {
        this.schoolPhone = schoolPhone;
    }

    public BigDecimal getFeeFrom() {
        return feeFrom;
    }

    public void setFeeFrom(BigDecimal feeFrom) {
        this.feeFrom = feeFrom;
    }

    public BigDecimal getFeeTo() {
        return feeTo;
    }

    public void setFeeTo(BigDecimal feeTo) {
        this.feeTo = feeTo;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getSchoolIntroduction() {
        return schoolIntroduction;
    }

    public void setSchoolIntroduction(String schoolIntroduction) {
        this.schoolIntroduction = schoolIntroduction;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getChildReceivingAgeMasterId() {
        return childReceivingAgeMasterId;
    }

    public void setChildReceivingAgeMasterId(Integer childReceivingAgeMasterId) {
        this.childReceivingAgeMasterId = childReceivingAgeMasterId;
    }

    public Integer getEducationMethodMasterId() {
        return educationMethodMasterId;
    }

    public void setEducationMethodMasterId(Integer educationMethodMasterId) {
        this.educationMethodMasterId = educationMethodMasterId;
    }

    public Integer getSchoolTypeMasterId() {
        return schoolTypeMasterId;
    }

    public void setSchoolTypeMasterId(Integer schoolTypeMasterId) {
        this.schoolTypeMasterId = schoolTypeMasterId;
    }

    public Integer getSchoolStatusMasterId() {
        return schoolStatusMasterId;
    }

    public void setSchoolStatusMasterId(Integer schoolStatusMasterId) {
        this.schoolStatusMasterId = schoolStatusMasterId;
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