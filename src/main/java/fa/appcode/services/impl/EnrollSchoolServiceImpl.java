package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.EnrollSchool;
import fa.appcode.entities.SchoolInfo;
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

    @Transactional(rollbackFor = Exception.class)
    public void enrollSchoolParent(AccountInfo account, SchoolInfo school, LocalDate enrollDate, String role, Principal principal) throws Exception {
        //create instant enroll School
        EnrollSchool schoolEnroll = new EnrollSchool();
        //AParent that enroll
        if (account.getDeleteFlg() || account.getStatusId().equals(Constant.STATUS_INACTIVE)) {
            throw new IllegalAccessException("Account is not active Or No Longer Available Please Try Again!");
        }
        if (school.getDeleteFlg() || !school.getStatusId().equals(Constant.SCHOOL_PUBLISH_STATUS)) {
            throw new IllegalAccessException("School is not published Or No Longer Available Please Try Again!");
        }
        if (role.equals(Constant.SCHOOL_OWNER_ROLE.toUpperCase().replace(" ", "_"))&& !validateAccess(school.getId(), principal).equals(Constant.SUCCESS)) {
            throw new IllegalAccessException(validateAccess(school.getId(), principal));
        }
        if (!isEnrolled(account.getId(), school.getId()).equals(Constant.SUCCESS)) {
            throw new IllegalAccessException(isEnrolled(account.getId(), school.getId()));
        }
        schoolEnroll.setAccount(account);
        //School that Parent will enroll
        schoolEnroll.setSchool(school);
        //Enroll Date
        schoolEnroll.setEnrollDate(enrollDate);
        //Start of Enroll is Always true
        schoolEnroll.setStatus(3);
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


    @Transactional(rollbackFor = Exception.class)
    public void evaluateParentEnroll(EnrollSchool enrollSchool, LocalDate approvalEnrollDate, String role, Integer status, Principal principal, Integer recordNo) throws Exception {
        if(enrollSchool==null){
            throw new IllegalAccessException("Illegal Unenroll school on your action");
        }
        if (role.equals(Constant.SCHOOL_OWNER_ROLE.toUpperCase().replace(" ", "_")) && !validateAccess(enrollSchool.getSchool().getId(), principal).equals(Constant.SUCCESS)) {
            throw new IllegalAccessException(validateAccess(enrollSchool.getSchool().getId(), principal));
        }

        int updateRows = enrollSchoolRepository.evaluateParentEnroll(enrollSchool.getId(), LocalDate.now(), role, Instant.now(), status, recordNo);
        if (updateRows == 0) {
            throw new IllegalAccessException("This Record is Already Edited. Please Try Again!");
        }
    }

    @Override
    public List<EnrolledSchoolVo> findParentRequestEnrollSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId) {
        return enrollSchoolRepository.findParentRequestEnrollSchoolByParentIdAndSchoolOwner(parentId, schoolOwnerId);
    }

    @Override
    public List<EnrolledSchoolVo> findParentRequestEnrolledSchoolByParentId(int id) {
        return enrollSchoolRepository.findParentRequestEnrolledSchoolByParentId(id);
    }

    //check for validation methods

    //check for authorized school owner school
    @Override
    public String validateAccess(Integer schoolId, Principal principal) {
        List<Integer> schoolIdList = schoolInfoService.getAllSchoolIdsForUnenrollParentByAccountEmail(principal.getName());
        if (!schoolIdList.contains(schoolId)) {
            return "School Onwer Does Not Have Access To This School";
        }
        return Constant.SUCCESS;
    }

    //check if Parent is Enrolled to school or not
    @Override
    public String isEnrolled(Integer parentId, Integer schoolId) {
        if (enrollSchoolRepository.isParentEnrollingToSchool(parentId, schoolId)) {
            return "Parent is currently already Enrolling to this School";
        }
        return Constant.SUCCESS;
    }
}
