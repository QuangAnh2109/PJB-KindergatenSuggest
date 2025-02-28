package fa.appcode.services.impl;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.repositories.EnrollSchoolRepository;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.EnrollSchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
public class EnrollSchoolServiceImpl implements EnrollSchoolService {
    @Autowired
    private EnrollSchoolRepository enrollSchoolRepository;

    @Transactional
    public void enrollSchool(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role) {

        //create instant enroll School
        EnrollSchool schoolEnroll = new EnrollSchool();
        //AParent that enroll
        schoolEnroll.setAccount(account);
        //School that Parent will enroll
        schoolEnroll.setSchool(school);
        //Enroll Date
        schoolEnroll.setEnrollDate(enrollDate);
        //Start of Enroll is Always true
        schoolEnroll.setStatus(2);
        //change with LocalDate time zone
//                schoolEnroll.setCreateTime(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
//                schoolEnroll.setUpdateTime(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        //set Create ID
        schoolEnroll.setCreateId(role);
        //set Create time
        schoolEnroll.setCreateTime(Instant.now());
        //set Update ID
        schoolEnroll.setUpdateId(role);
        //set Update Time
        schoolEnroll.setUpdateTime(Instant.now());
        schoolEnroll.setDeleteFlg(false);
        enrollSchoolRepository.save(schoolEnroll);
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

    @Transactional
    public void unenrollParentToSchool(EnrollSchool enrollSchool, LocalDate unenrollDate, String role) {
        //set unenroll date
        enrollSchool.setEnrollEndDate(unenrollDate);
        //set Update ID
        enrollSchool.setUpdateId(role);
        //set Update Time
        enrollSchool.setUpdateTime(Instant.now());
        //Unenroll Parent
        enrollSchool.setStatus(3);
        //set Record data +1
        enrollSchool.setRecordNo(enrollSchool.getRecordNo() + 1);
        enrollSchoolRepository.save(enrollSchool);
    }
}
