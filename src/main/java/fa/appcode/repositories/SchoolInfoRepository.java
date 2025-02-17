package fa.appcode.repositories;

import fa.appcode.entities.Schoolinfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SchoolInfoRepository extends JpaRepository<Schoolinfo, Integer> {
}
