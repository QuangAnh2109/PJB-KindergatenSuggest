package fa.appcode.repositories;

import fa.appcode.common.vo.CityVo;
import fa.appcode.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    @Query("SELECT new fa.appcode.common.vo.CityVo(c.id,c.cityName) FROM City c")
    public List<CityVo> getAllCities();

}
