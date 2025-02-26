package fa.appcode.services;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnrollSchoolService {
    EnrollSchool enrollSchool(EnrollSchool school);
    EnrollSchool findEnrollSchoolById(Integer id);
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id, Pageable pageable);
    Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId,String schoolOwnerId,Pageable pageable);
}
