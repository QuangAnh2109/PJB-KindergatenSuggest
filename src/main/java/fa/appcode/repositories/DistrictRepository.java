package fa.appcode.repositories;

import fa.appcode.common.vo.DistrictVo;
import fa.appcode.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query("SELECT new fa.appcode.common.vo.DistrictVo(d.id,d.districtName) FROM District d where d.city.id = ?1")
    public List<DistrictVo> getAllDistrictsByCityId(int cityId);
}
