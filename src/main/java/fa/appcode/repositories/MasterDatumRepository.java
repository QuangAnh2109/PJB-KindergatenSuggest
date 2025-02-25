package fa.appcode.repositories;

import fa.appcode.entities.MasterDatum;
import fa.appcode.common.vo.MasterDataVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("masterDatumRepository")
public interface MasterDatumRepository extends JpaRepository<MasterDatum,Integer> {

    List<MasterDataVo> findAllByTypeNameAndDeleteFlg(String typeName, Boolean deleteFlg);
}