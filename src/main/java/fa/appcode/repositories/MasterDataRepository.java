package fa.appcode.repositories;

import fa.appcode.entities.MasterDatum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("masterDataRepository")
public interface MasterDataRepository extends JpaRepository<MasterDatum,Integer> {


 /*   @Query("""
        SELECT md.typeValue 
        FROM MasterDatum md 
        WHERE md.typeName = :typeName AND md.typeKey = :typeKey AND md.deleteFlg = false
    """)
    String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey);
  */

    @Query("""
        SELECT md.typeValue 
        FROM MasterDatum md 
        WHERE md.id = :id  AND md.deleteFlg = false
    """)
    String getMasterById(Integer id);
}
