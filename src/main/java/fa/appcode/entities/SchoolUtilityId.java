package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@ToString
public class SchoolUtilityId implements java.io.Serializable {
    private static final long serialVersionUID = -8326683822496157736L;
    @Column(name = "school_id", nullable = false)
    private Integer schoolId;

    @Column(name = "utilities_id", nullable = false)
    private Integer utilitiesId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SchoolUtilityId entity = (SchoolUtilityId) o;
        return Objects.equals(this.utilitiesId, entity.utilitiesId) &&
                Objects.equals(this.schoolId, entity.schoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utilitiesId, schoolId);
    }

    public SchoolUtilityId(Integer schoolId, Integer utilitiesId) {
        this.utilitiesId = utilitiesId;
        this.schoolId = schoolId;
    }
}