package fa.appcode.repositories;

import fa.appcode.entities.Ward;
import fa.appcode.common.vo.WardVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<WardVo> findAllByDistrictIdAndDeleteFlg(Integer districtId, Boolean deleteFlg);

    WardVo findByIdAndDeleteFlg(int id, boolean deleteFlg);

    @Query("""
       SELECT w
       FROM Ward w 
       WHERE w.deleteFlg = false AND w.id = ?1
    """)
    Ward findByIdAndDeleteFlag(int id, boolean deleteFlg);
}
