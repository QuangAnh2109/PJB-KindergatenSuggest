package fa.appcode.repositories;

import fa.appcode.entities.Ward;
import fa.appcode.common.vo.WardVo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<WardVo> findAllByDistrictIdAndDeleteFlg(Integer districtId, Boolean deleteFlg);

    WardVo findByIdAndDeleteFlg(int id, boolean deleteFlg);
}
