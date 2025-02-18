package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class EnrollSchoolId implements java.io.Serializable {
    private static final long serialVersionUID = 5785588693013304289L;
    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "school_id", nullable = false)
    private Integer schoolId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EnrollSchoolId entity = (EnrollSchoolId) o;
        return Objects.equals(this.accountId, entity.accountId) &&
                Objects.equals(this.schoolId, entity.schoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, schoolId);
    }

}