package fa.appcode.repositories;

import fa.appcode.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {
    @Query("SELECT c.id,c.cityName FROM City c")
    public List<City> getAllCities();
}
