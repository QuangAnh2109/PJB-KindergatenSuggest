package fa.appcode.services.impl;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.MySchoolVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.repositories.EnrollSchoolRepository;
import fa.appcode.services.EnrollSchoolService;
import fa.appcode.services.SchoolInfoService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.Logger;
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
@AllArgsConstructor
public class EnrollSchoolServiceImpl implements EnrollSchoolService {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SchoolInfoService schoolInfoService;
    @Autowired
    private EnrollSchoolRepository enrollSchoolRepository;
    @Autowired
    private GlobalConfig globalConfig;

    private static final Logger LOGGER = Log4jUtils.getLogger(EnrollSchoolService.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enrollSchoolParent(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role, Principal principal) throws Exception {
        //create instant enroll School
        Log4jUtils.getLogger().info("Inside Enroll Method");
        EnrollSchool schoolEnroll = new EnrollSchool();
        //AParent that enroll
        if (account==null||account.getDeleteFlg() || account.getStatusId().equals(Constant.STATUS_INACTIVE)) {
            Log4jUtils.getLogger().info("Enroll Failed, account not active or no longer available");
            throw new IllegalAccessException("Account is not active Or No Longer Available Or not Exists Please Try Again!");
        }
        if (school==null||school.getDeleteFlg() || !school.getStatusId().equals(Constant.SCHOOL_PUBLISH_STATUS)) {
            Log4jUtils.getLogger().info("Enroll Failed, school not published or no longer available");
            throw new IllegalAccessException("School is not published Or No Longer Available Or not Exists Please Try Again!");
        }
        if (role.equals(Constant.SCHOOL_OWNER_ROLE.toUpperCase().replace(" ", "_")) && !validateAccess(school.getId(), principal)) {
            Log4jUtils.getLogger().info("Enroll Failed, school owner does not have access to SchoolID: " + school.getId());
            throw new IllegalAccessException(globalConfig.getSchoolOwnerAccess());
        }
        if (isEnrolled(account.getId(), school.getId())) {
            Log4jUtils.getLogger().info("Enroll Failed, Parent "+ account.getId()+" is already enrolling to school: " + school.getId());
            throw new IllegalAccessException(globalConfig.getParentEnrolled());
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
    public void evaluateParentEnroll(EnrollSchool enrollSchool, LocalDate approvalEnrollDate, String role, Integer status, Principal principal, Integer recordNo) throws Exception {
        if (enrollSchool == null) {
            Log4jUtils.getLogger().info("Enroll Failed, unenroll school is null");
            throw new IllegalAccessException("Illegal Unenroll school on your action");
        }
        if (role.equals(Constant.SCHOOL_OWNER_ROLE.toUpperCase().replace(" ", "_")) && !validateAccess(enrollSchool.getSchool().getId(), principal)) {
            Log4jUtils.getLogger().info("School Owner does not have Access to school: "+enrollSchool.getSchool().getId());
            throw new IllegalAccessException(globalConfig.getSchoolOwnerAccess());
        }

        int updateRows = enrollSchoolRepository.evaluateParentEnroll(enrollSchool.getId(), LocalDate.now(), role, Instant.now(), status, recordNo);
        if (updateRows == 0) {
            Log4jUtils.getLogger().info("Unenroll Fail, The Record has changed");
            throw new IllegalAccessException("This Record is Already Edited. Please Try Again!");
        }
    }

    //check for validation methods

    //check for authorized school owner school
    @Override
    public boolean validateAccess(Integer schoolId, Principal principal) {
        List<Integer> schoolIdList = schoolInfoService.getAllSchoolIdsForUnenrollParentByAccountEmail(principal.getName());
        return schoolIdList.contains(schoolId);
    }


    @Override
    public Page<MySchoolVo> findListSchoolParentEnrolledByParentId(int parentId, Pageable pageable) {
        LOGGER.info("Find List Parent Enrolled School By ParentId: {}", parentId);
        LOGGER.info("Found {} records of school for Parent ID: {}", pageable.getPageSize(), parentId);

        return enrollSchoolRepository.findListSchoolParentEnrolledByParentId(parentId,pageable);
    }

    @Override
    public Page<MySchoolVo> findListSchoolParentPreEnrolledByParentId(int parentId, Pageable pageable) {
        LOGGER.info("Find List Parent Previous Enrolled School By ParentId: {}", parentId);
        LOGGER.info("Found {} records of pre-school for Parent ID: {}", pageable.getPageSize(), parentId);
        return enrollSchoolRepository.findListSchoolParentPreEnrolledByParentId(parentId,pageable);
    }

    //check if Parent is Enrolled to school or not
    @Override
    public boolean isEnrolled(Integer parentId, Integer schoolId) {
        return enrollSchoolRepository.isParentEnrollingToSchool(parentId, schoolId);
    }
}
