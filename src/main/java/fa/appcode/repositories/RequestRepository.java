package fa.appcode.repositories;

import fa.appcode.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    Request findRequestsById(Integer id);
}
