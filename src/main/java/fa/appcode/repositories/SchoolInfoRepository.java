package fa.appcode.repositories;

import fa.appcode.entities.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("schoolInfoRepository")
public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Integer> {


    //find all schools by account email
    @Query("SELECT s FROM SchoolInfo s JOIN AccountInfo ai ON s.account.id = ai.id AND ai.email=?1 WHERE s.deleteFlg=false")
    List<SchoolInfo> findSchoolInfoByAccountEmail(String id);

    //find schoolInfo by school Id
    SchoolInfo findSchoolInfoById(int id);

    //find all schools by account email, get schoolID only
    @Query("SELECT s.id FROM SchoolInfo s JOIN AccountInfo ai ON s.account.id = ai.id AND ai.email=?1 WHERE s.deleteFlg=false")
    List<Integer> getAllSchoolIdsByAccountEmail(String id);

}
