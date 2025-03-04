package fa.appcode.services.impl;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.repositories.EnrollSchoolRepository;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.EnrollSchoolService;
import fa.appcode.services.SchoolInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollSchoolServiceImpl implements EnrollSchoolService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SchoolInfoService schoolInfoService;

    @Autowired
    private EnrollSchoolRepository enrollSchoolRepository;

    @Transactional
    public void enrollSchoolParent(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role) {

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
    public void evaluateParentEnroll(EnrollSchool enrollSchool, LocalDate approvalEnrollDate, String role, Integer status) {
        //set enroll Date
        if (status == 3) {
            enrollSchool.setEnrollDate(approvalEnrollDate);
        }
        //set Update ID
        if (status == 4) {
            enrollSchool.setEnrollEndDate(approvalEnrollDate);
        }
        enrollSchool.setUpdateId(role);
        //set Update Time
        enrollSchool.setUpdateTime(Instant.now());
        //set status of enroll parent school /reject,approve -> enrolled/ unenroll
        enrollSchool.setStatus(status);
        //set Record data +1
        enrollSchool.setRecordNo(enrollSchool.getRecordNo() + 1);
        enrollSchoolRepository.save(enrollSchool);
    }

    public String execute(String action, EnrollSchool enrollSchool, LocalDate date, String role, Principal principal) throws Exception {
        if (role.equals(Constant.SCHOOL_OWNER_ROLE.toUpperCase().replace(" ","_"))) {
            List<Integer> schoolIdList = schoolInfoService.getAllSchoolIdsForUnenrollParentByAccountEmail(principal.getName());
            if (!schoolIdList.contains(enrollSchool.getSchool().getId())) {
                throw new IllegalAccessException("Unauthorized action for this school.");
            }
        }
        EnrollSchoolService self = applicationContext.getBean(EnrollSchoolService.class);
        switch (action) {
            case Constant.UNENROLL_PARENT_SCHOOL -> self.evaluateParentEnroll(enrollSchool, date, role, 4); //execute unenroll parent
            case Constant.APPROVE_ENROLL_REQUEST -> self.evaluateParentEnroll(enrollSchool, date, role, 3); //execute approve enroll parent
            case Constant.REJECT_ENROLL_REQUEST -> self.evaluateParentEnroll(enrollSchool, date, role, 2); //execute reject enroll parent
        }
        return action;
    }

    @Override
    public List<EnrolledSchoolVo> findParentRequestEnrollSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId) {
        return enrollSchoolRepository.findParentRequestEnrollSchoolByParentIdAndSchoolOwner(parentId, schoolOwnerId);
    }

    @Override
    public List<EnrolledSchoolVo> findParentRequestEnrolledSchoolByParentId(int id) {
        return enrollSchoolRepository.findParentRequestEnrolledSchoolByParentId(id);
    }
}
