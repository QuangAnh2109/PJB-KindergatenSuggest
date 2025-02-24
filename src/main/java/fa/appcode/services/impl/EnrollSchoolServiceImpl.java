package fa.appcode.services.impl;

import fa.appcode.entities.EnrollSchool;
import fa.appcode.repositories.EnrollSchoolRepository;
import fa.appcode.services.EnrollSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollSchoolServiceImpl implements EnrollSchoolService {
    @Autowired
    private EnrollSchoolRepository enrollSchoolRepository;

    @Override
    public EnrollSchool enrollSchool(EnrollSchool school) {
        return enrollSchoolRepository.save(school);
    }
}
