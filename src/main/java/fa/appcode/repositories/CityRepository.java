package fa.appcode.repositories;

import fa.appcode.entities.City;
import fa.appcode.common.vo.CityVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {
    List<CityVo> findAllByDeleteFlg(Boolean deleteFlg);

    CityVo findByIdAndDeleteFlg(int id, boolean deleteFlg);

    @Query("""
       SELECT c
       FROM City c 
       WHERE c.deleteFlg = false AND c.id = ?1
    """)
    City findByIdAndDeleteFlag(int id, boolean deleteFlg);
}
