package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class EnrollschoolId implements java.io.Serializable {
    private static final long serialVersionUID = 4507153663795306803L;
    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "school_id", nullable = false)
    private Integer schoolId;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EnrollschoolId entity = (EnrollschoolId) o;
        return Objects.equals(this.accountId, entity.accountId) &&
                Objects.equals(this.schoolId, entity.schoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, schoolId);
    }

}