package fa.appcode.repositories;

import fa.appcode.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
