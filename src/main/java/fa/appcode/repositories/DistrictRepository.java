package fa.appcode.repositories;

import fa.appcode.entities.District;
import fa.appcode.common.vo.DistrictVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {
    List<DistrictVo> findAllByCityIdAndDeleteFlg(Integer cityId, Boolean deleteFlg);

    DistrictVo findByIdAndDeleteFlg(int id, boolean deleteFlg);

    @Query("""
       SELECT d
       FROM District d
       WHERE d.deleteFlg = false AND d.id = ?1
    """)
    District findByIdAndDeleteFalg(int id, boolean deleteFlg);
}
