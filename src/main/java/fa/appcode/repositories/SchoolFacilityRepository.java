package fa.appcode.repositories;

import fa.appcode.entities.SchoolFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchoolFacilityRepository extends JpaRepository<SchoolFacility, Integer> {
    @Query("SELECT sf.id.facilitiesId FROM SchoolFacility sf WHERE sf.id.schoolId=?1 AND sf.deleteFlg=?2")
    List<Integer> getAllSchoolFacilityIdBySchoolId(int schoolId, boolean deleteFlg);
}
