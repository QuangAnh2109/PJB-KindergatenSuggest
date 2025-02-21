package fa.appcode.repositories;

import fa.appcode.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    Request findRequestsById(Integer id);

    @Query("Select r from Request r where r.requestMasterId !=44")
    List<Request> findOpenedRequest();
}
