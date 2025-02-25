package fa.appcode.repositories;

import fa.appcode.entities.EnrollSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("enrollSchoolRepository")
public interface EnrollSchoolRepository extends JpaRepository<EnrollSchool, Integer> {

}
