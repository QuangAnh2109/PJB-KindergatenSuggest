package fa.appcode.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "accountinfo")
public class Accountinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "phone", length = 12)
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "role_master_id", nullable = false)
    private Integer roleMasterId;

    @Column(name = "account_status_master_id", nullable = false)
    private Integer accountStatusMasterId;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getRoleMasterId() {
        return roleMasterId;
    }

    public void setRoleMasterId(Integer roleMasterId) {
        this.roleMasterId = roleMasterId;
    }

    public Integer getAccountStatusMasterId() {
        return accountStatusMasterId;
    }

    public void setAccountStatusMasterId(Integer accountStatusMasterId) {
        this.accountStatusMasterId = accountStatusMasterId;
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