package fa.appcode.services.impl;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.exceptions.EnrollUnenrollParentException;
import fa.appcode.repositories.EnrollSchoolRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollSchoolServiceImpl implements EnrollSchoolService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SchoolInfoService schoolInfoService;

    @Autowired
    private EnrollSchoolRepository enrollSchoolRepository;
    @Autowired
    private GlobalConfig globalConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enrollSchoolParent(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role, Principal principal){
        //create instant enroll School
        Log4jUtils.getLogger().info("Inside Enroll Method");
        EnrollSchool schoolEnroll = new EnrollSchool();
        //AParent that enroll
        if (account==null||account.getDeleteFlg() || account.getStatusId().equals(Constant.STATUS_INACTIVE)) {
            Log4jUtils.getLogger().info("Enroll Failed, account not active or no longer available");
            throw new EnrollUnenrollParentException("Account is not active Or No Longer Available Or not Exists Please Try Again!",account.getId());
        }
        if (school==null||school.getDeleteFlg() || !school.getStatusId().equals(Constant.SCHOOL_PUBLISH_STATUS)) {
            Log4jUtils.getLogger().info("Enroll Failed, school not published or no longer available");
            throw new EnrollUnenrollParentException("School is not published Or No Longer Available Or not Exists Please Try Again!",account.getId());
        }
        if (role.equals(Constant.SCHOOL_OWNER_ROLE.toUpperCase().replace(" ", "_")) && !validateAccess(school.getId(), principal)) {
            Log4jUtils.getLogger().info("Enroll Failed, school owner does not have access to SchoolID: " + school.getId());
            throw new EnrollUnenrollParentException(globalConfig.getSchoolOwnerAccess(),account.getId());
        }
        if (isEnrolled(account.getId(), school.getId())) {
            Log4jUtils.getLogger().info("Enroll Failed, Parent "+ account.getId()+" is already enrolling to school: " + school.getId());
            throw new EnrollUnenrollParentException(globalConfig.getParentEnrolled(),account.getId());
        }
        schoolEnroll.setAccount(account);
        //School that Parent will enroll
        schoolEnroll.setSchool(school);
        //Enroll Date
        schoolEnroll.setEnrollDate(enrollDate);
        //Start of Enroll is Always true
        schoolEnroll.setStatus(Constant.ENROLL_STATUS_ENROLL);
        //change with LocalDate time zone
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
        Log4jUtils.getLogger().info("Enroll Success");
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void evaluateParentEnroll(EnrollSchool enrollSchool, LocalDate approvalEnrollDate, String role, Integer status, Principal principal, Integer recordNo, Integer parentId){
        if (enrollSchool == null) {
            Log4jUtils.getLogger().info("Enroll Failed, unenroll school is null");
            throw new EnrollUnenrollParentException("Illegal Unenroll school on your action",parentId);
        }
        if (role.equals(Constant.SCHOOL_OWNER_ROLE.toUpperCase().replace(" ", "_")) && !validateAccess(enrollSchool.getSchool().getId(), principal)) {
            Log4jUtils.getLogger().info("School Owner does not have Access to school: "+enrollSchool.getSchool().getId());
            throw new EnrollUnenrollParentException(globalConfig.getSchoolOwnerAccess(),parentId);
        }

        int updateRows = enrollSchoolRepository.evaluateParentEnroll(enrollSchool.getId(), LocalDate.now(), role, Instant.now(), status, recordNo);
        if (updateRows == 0) {
            Log4jUtils.getLogger().info("Unenroll Fail, The Record has changed");
            throw new EnrollUnenrollParentException("This Record is Already Edited. Please Try Again!",parentId);
        }
    }

    //check for validation methods

    //check for authorized school owner school
    @Override
    public boolean validateAccess(Integer schoolId, Principal principal) {
        List<Integer> schoolIdList = schoolInfoService.getAllSchoolIdsForUnenrollParentByAccountEmail(principal.getName());
        return schoolIdList.contains(schoolId);
    }

    //check if Parent is Enrolled to school or not
    @Override
    public boolean isEnrolled(Integer parentId, Integer schoolId) {
        return enrollSchoolRepository.isParentEnrollingToSchool(parentId, schoolId);
    }
}
