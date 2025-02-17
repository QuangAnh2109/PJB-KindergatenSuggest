package fa.appcode.repositories;

import fa.appcode.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
