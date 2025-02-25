package fa.appcode.repositories;

import fa.appcode.entities.District;
import fa.appcode.common.vo.DistrictVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<DistrictVo> findAllByCityIdAndDeleteFlg(Integer cityId, Boolean deleteFlg);
}
