package fa.appcode.repositories;

import fa.appcode.entities.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Integer> {
}
