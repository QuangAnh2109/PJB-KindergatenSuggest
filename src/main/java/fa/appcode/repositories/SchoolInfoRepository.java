package fa.appcode.repositories;

import fa.appcode.entities.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Integer> {
}
