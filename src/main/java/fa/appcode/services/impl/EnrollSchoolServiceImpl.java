package fa.appcode.services.impl;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.repositories.EnrollSchoolRepository;
import fa.appcode.services.EnrollSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollSchoolServiceImpl implements EnrollSchoolService {
    @Autowired
    private EnrollSchoolRepository enrollSchoolRepository;

    @Transactional
    public EnrollSchool enrollSchool(EnrollSchool school) {
        return enrollSchoolRepository.save(school);
    }

    @Override
    public EnrollSchool findEnrollSchoolById(Integer id) {
        return enrollSchoolRepository.findEnrollSchoolById(id);
    }
    @Override
    public Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id, Pageable pageable) {
        return enrollSchoolRepository.findParentEnrolledSchoolByParentId(id, pageable);
    }

    @Override
    public Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId, Pageable pageable) {
        return enrollSchoolRepository.findParentEnrolledSchoolByParentIdAndSchoolOwner(parentId, schoolOwnerId, pageable);
    }
}
