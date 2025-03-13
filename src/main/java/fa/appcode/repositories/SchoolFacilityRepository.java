package fa.appcode.repositories;

import fa.appcode.entities.SchoolFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolFacilityRepository extends JpaRepository<SchoolFacility, Long> {


    List<SchoolFacility> getSchoolFacilitiesBySchool_Id(Integer schoolId);
}
