package fa.appcode.repositories;

import fa.appcode.common.vo.SchoolInfoVo;
import fa.appcode.entities.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("schoolInfoRepository")
public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Integer> {


    //find all schools by account email that have school status of published
    @Query("SELECT s FROM SchoolInfo s JOIN AccountInfo ai ON s.account.id = ai.id WHERE ai.email=?1 AND s.deleteFlg=false AND s.statusId=5")
    List<SchoolInfo> findSchoolInfoByAccountEmail(String id);

    //find schoolInfo by school Id
    @Query("SELECT s FROM SchoolInfo s WHERE s.deleteFlg=false AND s.statusId=5 AND s.id=?1")
    SchoolInfo findSchoolInfoById(int id);

    //find all schools by account email, get schoolID only
    @Query("SELECT s.id FROM SchoolInfo s JOIN AccountInfo ai ON s.account.id = ai.id WHERE ai.email=?1 AND s.deleteFlg=false AND s.statusId=5")
    List<Integer> getAllSchoolIdsByAccountEmail(String id);

    //find all school published for admin enroll parent to school
    @Query("SELECT s FROM SchoolInfo s WHERE s.deleteFlg=false AND s.statusId=5")
    List<SchoolInfo> findAllSchoolPublished();

    //get all school by account email regard school status
    @Query("SELECT s.id FROM SchoolInfo s JOIN AccountInfo ai ON s.account.id = ai.id WHERE ai.email=?1 AND s.deleteFlg=false")
    List<Integer> getAllSchoolIdsForUnenrollParentByAccountEmail(String id);

    SchoolInfoVo findSchoolInfoVoByIdAndDeleteFlg(int schoolId, boolean deleteFlg);

    SchoolInfoVo findSchoolInfoVoByIdAndAccountIdAndDeleteFlg(int schoolId, int accountId, boolean deleteFlg);

    SchoolInfo findSchoolInfoByIdAndDeleteFlg(int id, boolean deleteFlg);

    SchoolInfo findSchoolInfoByIdAndAccountIdAndDeleteFlg(int id, int accountId, boolean deleteFlg);
}