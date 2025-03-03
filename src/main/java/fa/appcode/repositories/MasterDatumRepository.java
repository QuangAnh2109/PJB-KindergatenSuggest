package fa.appcode.repositories;

import fa.appcode.entities.MasterDatum;
import fa.appcode.common.vo.MasterDataVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("masterDatumRepository")
public interface MasterDatumRepository extends JpaRepository<MasterDatum, Integer> {

    List<MasterDataVo> findAllMasterDataVoByTypeNameAndDeleteFlg(String typeName, boolean deleteFlg);

    List<MasterDataVo> findAllMasterDataVoByTypeNameInAndDeleteFlg(List<String> typeName, boolean deleteFlg);

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

    @Query("""
                SELECT md
                FROM MasterDatum md 
                WHERE md.typeName = :typeName AND md.deleteFlg = false
            """)
    List<MasterDatum> getMasterByTypeName(String typeName);
}