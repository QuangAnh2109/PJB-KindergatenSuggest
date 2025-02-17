package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class SchoolfacilityId implements java.io.Serializable {
    private static final long serialVersionUID = 8446333099002379691L;
    @Column(name = "school_id", nullable = false)
    private Integer schoolId;

    @Column(name = "facilities_master_id", nullable = false)
    private Integer facilitiesMasterId;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getFacilitiesMasterId() {
        return facilitiesMasterId;
    }

    public void setFacilitiesMasterId(Integer facilitiesMasterId) {
        this.facilitiesMasterId = facilitiesMasterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SchoolfacilityId entity = (SchoolfacilityId) o;
        return Objects.equals(this.facilitiesMasterId, entity.facilitiesMasterId) &&
                Objects.equals(this.schoolId, entity.schoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facilitiesMasterId, schoolId);
    }

}