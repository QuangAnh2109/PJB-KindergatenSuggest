package fa.appcode.repositories;

import fa.appcode.entities.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByAccountInfo_Id(Integer accountInfoId, Pageable pageable);
}
