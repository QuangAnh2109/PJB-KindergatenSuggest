package fa.appcode.repositories;

import fa.appcode.entities.MasterDatum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("masterDatumRepository")
public interface MasterDatumRepository extends JpaRepository<MasterDatum,Integer> {

}
