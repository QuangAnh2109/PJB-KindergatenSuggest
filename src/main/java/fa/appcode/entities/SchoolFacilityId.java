package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class SchoolFacilityId implements java.io.Serializable {
    private static final long serialVersionUID = -2057415372289811956L;
    @Column(name = "school_id", nullable = false)
    private Integer schoolId;

    @Column(name = "facilities_id", nullable = false)
    private Integer facilitiesId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SchoolFacilityId entity = (SchoolFacilityId) o;
        return Objects.equals(this.facilitiesId, entity.facilitiesId) &&
                Objects.equals(this.schoolId, entity.schoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facilitiesId, schoolId);
    }

    public SchoolFacilityId(Integer schoolId, Integer facilitiesId) {
        this.schoolId = schoolId;
        this.facilitiesId = facilitiesId;
    }
}