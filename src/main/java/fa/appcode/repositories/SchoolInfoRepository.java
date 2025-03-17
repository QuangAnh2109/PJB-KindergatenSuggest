package fa.appcode.repositories;

import fa.appcode.common.vo.*;
import fa.appcode.entities.SchoolInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    //find all SchoolListManager by paging and search and delete flag
    @Query("""
            SELECT new fa.appcode.common.vo.SchoolListManager(si.id, si.schoolName, si.schoolAddress, si.city.cityName, si.district.districtName, si.ward.wardName, si.schoolPhone, si.schoolEmail, si.postedDate, si.statusId,
            CASE WHEN si.statusId = 7 THEN false ELSE true END, si.recordNo)
            FROM SchoolInfo si
            WHERE (si.schoolName LIKE %:search% OR si.schoolAddress LIKE %:search% OR si.city.cityName LIKE %:search% OR si.district.districtName LIKE %:search% OR si.ward.wardName LIKE %:search% OR si.schoolPhone LIKE %:search% OR si.schoolEmail LIKE %:search%)
                AND si.deleteFlg = :deleteFlg
            ORDER BY
            CASE WHEN si.statusId = 2 THEN 0 ELSE 1 END,
            si.postedDate DESC""")
    Page<SchoolListManager> searchAllByNameAndPagingAndDeleteFlg(Pageable pageable, @Param("search") String search, @Param("deleteFlg") boolean deleteFlg);

    //find all SchoolListManager by paging and search and account id and delete flag
    @Query("""
            SELECT new fa.appcode.common.vo.SchoolListManager(si.id, si.schoolName, si.schoolAddress, si.city.cityName, si.district.districtName, si.ward.wardName, si.schoolPhone, si.schoolEmail, si.postedDate, si.statusId,
            CASE WHEN si.statusId IN (3,7) THEN false ELSE true END, si.recordNo)
            FROM SchoolInfo si
            WHERE (si.schoolName LIKE %:search% OR si.schoolAddress LIKE %:search% OR si.city.cityName LIKE %:search% OR si.district.districtName LIKE %:search% OR si.ward.wardName LIKE %:search% OR si.schoolPhone LIKE %:search% OR si.schoolEmail LIKE %:search%)
                AND si.deleteFlg = :deleteFlg
                AND si.account.email = :account
            ORDER BY
            CASE WHEN si.statusId = 2 THEN 0 ELSE 1 END,
            si.postedDate DESC
            """)
    Page<SchoolListManager> searchAllByNameAndAccountAndPagingAndDeleteFlg(Pageable pageable, @Param("search") String search, @Param("account") String email, @Param("deleteFlg") boolean deleteFlg);

    //update school status by school id and record no and no delete
    @Modifying
    @Query("UPDATE SchoolInfo si SET si.statusId = :#{#request.schoolStatus}, si.updateId = :#{#request.updateId}, si.updateTime = :#{#request.updateTime}, si.recordNo = si.recordNo + 1 " +
            "WHERE si.id = :#{#request.id} " +
            "AND si.recordNo = :#{#request.recordNo} " +
            "AND si.deleteFlg = :#{#request.deleteFlg} " +
            "AND (:#{#request.email} IS NULL OR si.account.id = (SELECT ai.id FROM AccountInfo ai WHERE ai.email = :#{#request.email})) " +
            "AND si.statusId IN (:#{#request.statusList})")
    int updateSchoolStatusByRequest(@Param("request") SchoolStatusUpdateRequest request);


    @Query("""
            SELECT new fa.appcode.common.vo.SchoolFormManager(si.id, si.schoolName, si.typeId, si.schoolAddress, si.city.id, si.city.cityName, si.district.id, si.district.districtName, si.ward.id, si.ward.wardName, si.schoolEmail, si.schoolPhone, si.childReceivingAgeId, si.educationMethodId, si.feeTo, si.feeFrom, si.schoolIntroduction, si.imageUrl, si.updateTime, si.updateId, si.recordNo, si.deleteFlg, si.account.email, si.statusId)
            FROM SchoolInfo si
            WHERE si.id = ?1
            AND si.deleteFlg = ?2
            """)
    SchoolFormManager getSchoolFormByIdAndDeleteFlg(int id, boolean deleteFlg);

    @Query("""
            SELECT new fa.appcode.common.vo.SchoolFormManager(si.id, si.schoolName, si.typeId, si.schoolAddress, si.city.id, si.city.cityName, si.district.id, si.district.districtName, si.ward.id, si.ward.wardName, si.schoolEmail, si.schoolPhone, si.childReceivingAgeId, si.educationMethodId, si.feeTo, si.feeFrom, si.schoolIntroduction, si.imageUrl, si.updateTime, si.updateId, si.recordNo, si.deleteFlg, si.account.email, si.statusId)
            FROM SchoolInfo si
            WHERE si.id = ?1
            AND (?2 IS NULL OR si.account.email = ?2)
            AND si.deleteFlg = ?3
            """)
    SchoolFormManager getSchoolFormBySchoolIdAndEmailAndDeleteFlg(int id, String email, boolean deleteFlg);

    @Modifying
    @Query("UPDATE SchoolInfo si SET si.schoolName = :#{#schoolInfo.name} " +
            ",si.typeId = :#{#schoolInfo.typeId} " +
            ",si.schoolAddress = :#{#schoolInfo.address} " +
            ",si.city.id = :#{#schoolInfo.cityId} " +
            ",si.district.id = :#{#schoolInfo.districtId} " +
            ",si.ward.id = :#{#schoolInfo.wardId} " +
            ",si.schoolEmail = :#{#schoolInfo.email} " +
            ",si.schoolPhone = :#{#schoolInfo.phone} " +
            ",si.childReceivingAgeId = :#{#schoolInfo.childReceivingAgeId} " +
            ",si.educationMethodId = :#{#schoolInfo.educationMethodId} " +
            ",si.feeTo = :#{#schoolInfo.feeTo} " +
            ",si.feeFrom = :#{#schoolInfo.feeFrom} " +
            ",si.schoolIntroduction = :#{#schoolInfo.introduction} " +
            ",si.imageUrl = :#{#schoolInfo.imgageUrl} " +
            ",si.updateTime = :#{#schoolInfo.updateTime} " +
            ",si.updateId = :#{#schoolInfo.updateId} " +
            ",si.recordNo = si.recordNo + 1 " +
            ",si.statusId = :#{#schoolInfo.statusId} " +
            "WHERE si.id = :#{#schoolInfo.id} " +
            "AND si.recordNo = :#{#schoolInfo.recordNo} " +
            "AND si.deleteFlg = :#{#schoolInfo.deleteFlg} " +
            "AND (:#{#schoolInfo.schoolOwnerEmail} IS NULL OR si.account.id = (SELECT ai.id FROM AccountInfo ai WHERE ai.email = :#{#schoolInfo.schoolOwnerEmail})) ")
    int updateSchoolInfoBySchoolFormManager(@Param("schoolInfo") SchoolFormManager schoolFormManager);

    @Query("""
            SELECT new fa.appcode.common.vo.SchoolRatingFeedback
            (
                f.id.schoolId,
                AVG((f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5),
                AVG(f.learningProgram),
                AVG(f.facilitiesUtilities),
                AVG(f.extracurricularActivities),
                AVG(f.teacherStaff),
                AVG(f.hygieneNutrition),
                COUNT(f.id.accountId)
            )
            FROM Feedback f
            WHERE f.id.feedbackTime = (
                SELECT MAX(f2.id.feedbackTime)
                FROM Feedback f2
                WHERE
                    f2.id.accountId = f.id.accountId
                    AND f2.id.schoolId = :#{#form.schoolId}
                    AND (:#{#form.accountEmail} IS NULL OR f2.school.account.email = :#{#form.accountEmail})
                    AND (:#{#form.fromDate} IS NULL OR f.id.feedbackTime >= :#{#form.fromDate})
                    AND (:#{#form.toDate} IS NULL OR f.id.feedbackTime <= :#{#form.toDate})
                GROUP BY f2.id.accountId
                )
                AND f.id.schoolId = :#{#form.schoolId}
            GROUP BY f.id.schoolId""")
    SchoolRatingFeedback getSchoolRatingFeedbackBySchoolId(@Param("form")FeedbackRatingRequest feedbackRatingRequest);

    @Query("""
            SELECT new fa.appcode.common.vo.AccountFeedback
            (
                f.accountInfo.fullName,
                MAX(f.id.feedbackTime),
                (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5,
                f.feedbackMessage
            )
            FROM Feedback f
            WHERE f.id.schoolId = :#{#form.schoolId}
                AND (:#{#form.accountEmail} IS NULL OR f.school.account.email = :#{#form.accountEmail})
                AND f.deleteFlg = :#{#form.deleteFlg}
                AND ((:#{#form.fromDate} IS NULL OR f.id.feedbackTime >= :#{#form.fromDate}) AND (:#{#form.toDate} IS NULL OR f.id.feedbackTime <= :#{#form.toDate}))
                AND (:#{#form.rating} IS NULL
                OR
                (
                    (1 IN :#{#form.rating} AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 >= 1 AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 < 2)
                    OR (2 IN :#{#form.rating} AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 >= 2 AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 < 3)
                    OR (3 IN :#{#form.rating} AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 >= 3 AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 < 4)
                    OR (4 IN :#{#form.rating} AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 >= 4 AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 < 5)
                    OR (5 IN :#{#form.rating} AND (f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 = 5)
                ))
            GROUP BY f.id.accountId
            ORDER BY f.id.feedbackTime""")
    Page<AccountFeedback> getAllAccountFeedbackBySchoolId(@Param("form")FeedbackRatingRequest feedbackRatingRequest, Pageable pageable);

    @Query("SELECT si.schoolName FROM SchoolInfo si WHERE si.id = ?1 AND si.deleteFlg = ?2")
    String getSchoolNameByIdAndDeleteFlg(int id, boolean deleteFlg);

    List<SchoolInfo> findAll();
}