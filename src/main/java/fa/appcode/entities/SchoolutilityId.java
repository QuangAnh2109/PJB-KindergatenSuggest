package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class SchoolutilityId implements java.io.Serializable {
    private static final long serialVersionUID = -621343396855351684L;
    @Column(name = "school_id", nullable = false)
    private Integer schoolId;

    @Column(name = "utilities_master_id", nullable = false)
    private Integer utilitiesMasterId;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getUtilitiesMasterId() {
        return utilitiesMasterId;
    }

    public void setUtilitiesMasterId(Integer utilitiesMasterId) {
        this.utilitiesMasterId = utilitiesMasterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SchoolutilityId entity = (SchoolutilityId) o;
        return Objects.equals(this.utilitiesMasterId, entity.utilitiesMasterId) &&
                Objects.equals(this.schoolId, entity.schoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utilitiesMasterId, schoolId);
    }

}