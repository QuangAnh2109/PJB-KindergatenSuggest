package fa.appcode.repositories;

import fa.appcode.common.vo.FeedbackListVo;
import fa.appcode.common.vo.RatingVo;
import fa.appcode.entities.Feedback;
import fa.appcode.entities.FeedbackId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("""
    SELECT new fa.appcode.common.vo.RatingVo(
                COALESCE((ROUND(AVG(f.learningProgram) * 2, 0) / 2),0.0),
                COALESCE((ROUND(AVG(f.facilitiesUtilities) * 2, 0) / 2),0.0),
                COALESCE((ROUND(AVG(f.extracurricularActivities) * 2, 0) / 2),0.0),
                COALESCE((ROUND(AVG(f.teacherStaff) * 2, 0) / 2),0.0),
                COALESCE((ROUND(AVG(f.hygieneNutrition) * 2, 0) / 2),0.0)) 
    FROM Feedback f
    WHERE f.school.id = :schoolId 
    AND f.id.feedbackTime = (
        SELECT MAX(f2.id.feedbackTime)
        FROM Feedback f2 
        WHERE f2.school.id = :schoolId 
        AND f2.id.accountId = f.id.accountId
    )
""")
    RatingVo findRatingBySchoolId(Integer schoolId);

    @Query("""
        SELECT new fa.appcode.common.vo.FeedbackListVo(f.id.accountId,f.accountInfo.fullName,f.accountInfo.imageUrl,f.id.feedbackTime,f.learningProgram,f.facilitiesUtilities,f.extracurricularActivities,f.teacherStaff,f.hygieneNutrition,
        COALESCE(CAST((f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 AS double), 0.0)
        ,f.feedbackMessage)
        FROM Feedback f
        JOIN AccountInfo ai ON f.id.accountId = ai.id
        WHERE f.id.schoolId = :schoolId
        AND (COALESCE(CAST((f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 AS double), 0.0) >= :minRating
        AND COALESCE(CAST((f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 AS double), 0.0) < :maxRating)
        AND f.id.feedbackTime  = (
            SELECT MAX(f2.id.feedbackTime)
            FROM Feedback f2
            WHERE f2.school.id = :schoolId
            AND f2.id.accountId = f.id.accountId
        ) 
    """)
    List<FeedbackListVo> findListFeedbackBySchoolIdAndRating(@Param("schoolId") Integer schoolId, @Param("minRating") Double minRating, @Param("maxRating") Double maxRating);

    @Query("""
        SELECT new fa.appcode.common.vo.FeedbackListVo(f.id.accountId,f.accountInfo.fullName,f.accountInfo.imageUrl,f.id.feedbackTime,f.learningProgram,f.facilitiesUtilities,f.extracurricularActivities,f.teacherStaff,f.hygieneNutrition,
        COALESCE(CAST((f.learningProgram + f.facilitiesUtilities + f.extracurricularActivities + f.teacherStaff + f.hygieneNutrition)/5 AS double), 0.0)
        ,f.feedbackMessage)
        FROM Feedback f
        JOIN AccountInfo ai ON f.id.accountId = ai.id
        WHERE f.id.schoolId = :schoolId AND f.id.feedbackTime = (
            SELECT MAX(f2.id.feedbackTime)
            FROM Feedback f2
            WHERE f2.school.id = :schoolId
            AND f2.id.accountId = f.id.accountId
        )
        ORDER BY f.id.feedbackTime DESC
    """)
    List<FeedbackListVo> findListFeedbackBySchoolId(@Param("schoolId") Integer schoolId);
}
