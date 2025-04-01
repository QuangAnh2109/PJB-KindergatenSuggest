package fa.appcode.repositories;

import fa.appcode.entities.SchoolUtility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchoolUtilityRepository extends JpaRepository<SchoolUtility, Integer> {
    @Query("SELECT su.id.utilitiesId FROM SchoolUtility su WHERE su.id.schoolId=?1 AND su.deleteFlg=?2")
    List<Integer> getAllSchoolUtilityIdBySchoolId(int schoolId, boolean deleteFlg);

    List<SchoolUtility> findBySchoolId(Integer id);
}
