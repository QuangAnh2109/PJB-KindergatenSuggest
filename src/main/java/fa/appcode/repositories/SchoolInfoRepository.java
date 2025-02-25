package fa.appcode.repositories;

import fa.appcode.entities.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("schoolInfoRepository")
public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Integer> {
    @Query("SELECT s FROM SchoolInfo s JOIN AccountInfo ai ON s.account.id = ai.id WHERE s.deleteFlg=false")
    List<SchoolInfo> findSchoolInfoByAccountId(int id);

    @Query("SELECT s FROM SchoolInfo s JOIN AccountInfo ai ON s.account.id = ai.id AND ai.email=?1 WHERE s.deleteFlg=false")
    List<SchoolInfo> findSchoolInfoByAccountId(String id);

    List<SchoolInfo> findAll();

    SchoolInfo findSchoolInfoById(int id);
}
