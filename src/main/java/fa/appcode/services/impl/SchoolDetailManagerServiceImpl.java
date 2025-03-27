package fa.appcode.services.impl;

import fa.appcode.common.utils.*;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.*;
import fa.appcode.repositories.*;
import fa.appcode.services.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchoolDetailManagerServiceImpl implements SchoolDetailManagerService {

    private final CityService cityService;

    private final WardService wardService;

    private final DistrictService districtService;

    private final MasterDatumService masterDatumService;

    private final SchoolInfoService schoolInfoService;

    private final SchoolFacilityService schoolFacilityService;

    private final SchoolUtilityService schoolUtilityService;

    private final GlobalConfig globalConfig;

    private final EmailService emailService;

    private final AccountService accountService;

    private final SchoolInfoRepository schoolInfoRepository;

    private final Logger log = LoggerFactory.getLogger(SchoolDetailManagerServiceImpl.class);

    @Override
    public String getSchoolCreateFormToModel(Model model) {
        model.addAttribute("citys", cityService.findAllByNoDelete());
        model.addAttribute("serverLink", globalConfig.getServerLink());
        masterDatumService.setMasterDataToModel(model);
        return Constant.SCHOOL_CREATE_PAGE;
    }

    @Override
    public void setAllAddressToModel(Model model, int cityId, int districtId) {
        model.addAttribute("citys", cityService.findAllByNoDelete());
        model.addAttribute("districts", districtService.findAllByCityIdAndNoDelete(cityId));
        model.addAttribute("wards", wardService.findAllByDistrictIdAndNoDelete(districtId));
        masterDatumService.setMasterDataToModel(model);
    }

    @Override
    public String getSchoolDetail(Model model, int schoolId) {
        // Get the current user's authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(), role = RoleConstant.FROM_BUTTON_OWNER;
        for(GrantedAuthority grantedAuthority: authentication.getAuthorities()){
            if(grantedAuthority.getAuthority().equals(Constant.ADMIN_ROLE)){
                role = RoleConstant.FROM_BUTTON_ADMIN;
                email = null;
                break;
            }
        }

        SchoolFormManager school = schoolInfoService.getSchoolFormBySchoolIdAndEmailAndNoDelete(schoolId, email);
        SchoolFormButton.valueOf(role+school.getStatusId()).getSchoolFormButtonBuild().setButton(model);
        model.addAttribute("school", school);
        setAllAddressToModel(model, school.getCityId(), school.getDistrictId());


        model.addAttribute("statusName", masterDatumService.getMasterByTypeNameAndTypeKey(SchoolConstant.SCHOOL_STATUS, school.getStatusId()));
        model.addAttribute("schoolFacilities", schoolFacilityService.getAllSchoolFacilityIdBySchoolIdAndNoDeleteFlg(school.getId()));
        model.addAttribute("schoolUtilities", schoolUtilityService.getAllSchoolUtilityIdBySchoolIdAndNoDelete(school.getId()));

        // Add city list to model
        model.addAttribute("citys", cityService.findAllByNoDelete());
        return Constant.SCHOOL_DETAIL_MANAGER_PAGE;
    }

    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> createNewSchool(SchoolInfo schoolInfo, MultipartFile image, List<Integer> schoolFacilityId, List<Integer> schoolUtilityId) throws DataAccessException {
        Map<String, Object> responseSuccess = new HashMap<>(Map.of("id", schoolInfo.getId())), responseFailed = new HashMap<>();
        if(schoolInfo.getStatusId()==SchoolConstant.STATUS_SUBMITTED){
            responseSuccess.put("message", globalConfig.getSubmitSuccess());
            responseFailed.put("message", globalConfig.getSubmitFailed());
        } else{
            responseSuccess.put("message", globalConfig.getSaveSchoolSuccess());
            responseFailed.put("message", globalConfig.getSaveSchoolFailed());
        }

        try {
            schoolInfoRepository.save(schoolInfo);
            schoolUtilityService.saveAllSchoolUtility(schoolUtilityId, schoolInfo);
            schoolFacilityService.saveAllSchoolFacility(schoolFacilityId,schoolInfo);
            if(schoolInfo.getStatusId()==SchoolConstant.STATUS_SUBMITTED){
                sendEmailForSubmitted(schoolInfo.getId());
            }
            schoolInfo.setImageUrl(saveImage(image, schoolInfo.getId()));
            schoolInfoRepository.save(schoolInfo);

            return ResponseEntity.ok().body(responseSuccess);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok().body(responseFailed);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> updateSchool(SchoolFormManager schoolInfo, MultipartFile image, List<Integer> schoolFacilityId, List<Integer> schoolUtilityId) throws DataAccessException {
        // Get the current user's authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int status = SchoolConstant.STATUS_SUBMITTED;
        for(GrantedAuthority grantedAuthority: authentication.getAuthorities()){
            if(grantedAuthority.getAuthority().equals(Constant.ADMIN_ROLE)){
                status = SchoolConstant.STATUS_APPROVED;
                break;
            }
        }
        schoolInfo.setStatusId(status);

        // Get school info from DB
        SchoolInfo schoolInfoDb = schoolInfoRepository.findSchoolInfoByIdAndRecordNoAndDeleteFlg(schoolInfo.getId(), schoolInfo.getRecordNo(), false);
        List<Integer> facilityIdDb = schoolFacilityService.getAllSchoolFacilityIdBySchoolIdAndNoDeleteFlg(schoolInfo.getId()), utilityIdDb = schoolUtilityService.getAllSchoolUtilityIdBySchoolIdAndNoDelete(schoolInfo.getId());
        if(schoolInfoDb == null) return ResponseEntity.badRequest().body(Map.of("message", "School not found!"));
        
        // Check if there is no change
        SchoolFormManager schoolInfoNowForm = new SchoolFormManager(
                false, null, schoolInfoDb.getRecordNo(), schoolInfoDb.getId(),
                schoolInfoDb.getTypeId(), schoolInfoDb.getSchoolName(), schoolInfoDb.getSchoolAddress(), schoolInfoDb.getCity().getId(), schoolInfoDb.getDistrict().getId(), schoolInfoDb.getWard().getId(),
                schoolInfoDb.getSchoolEmail(), schoolInfoDb.getSchoolPhone(), schoolInfoDb.getChildReceivingAgeId(), schoolInfoDb.getEducationMethodId(), schoolInfoDb.getFeeTo(),
                schoolInfoDb.getFeeFrom(), schoolInfoDb.getSchoolIntroduction(), schoolInfoDb.getImageUrl(), schoolInfo.getUpdateTime(), schoolInfo.getUpdateId()
        );
        if(!schoolInfoNowForm.equals(schoolInfo) && !new HashSet<>(facilityIdDb).equals(new HashSet<>(schoolFacilityId)) && !new HashSet<>(utilityIdDb).equals(new HashSet<>(schoolUtilityId))) return ResponseEntity.badRequest().body(Map.of("message", "Don't have change!"));
        try {
            schoolInfo.setImgageUrl(saveImage(image, schoolInfo.getId()));

            schoolInfoService.updateSchoolInfoBySchoolFormManager(schoolInfo);
            schoolUtilityService.saveAllSchoolUtility(schoolUtilityId, schoolInfoDb);
            schoolFacilityService.saveAllSchoolFacility(schoolFacilityId,schoolInfoDb);
            if(schoolInfo.getStatusId() == SchoolConstant.STATUS_SUBMITTED){
                sendEmailForSubmitted(schoolInfo.getId());
            }
            return ResponseEntity.ok().body(Map.of("message", "Update school successfully!"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save school due to file upload error.", e);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeSchoolStatus(Integer id, int recordNo, int newStatus, List<Integer> inStatus, Integer mailId, Map<Placeholder, String> detail, List<String> toMail, List<String> ccMail) throws DataAccessException {
        // Get role
        String role = RoleConstant.SCHOOL_OWNER;
        for(GrantedAuthority grantedAuthority: SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
            if(grantedAuthority.getAuthority().equals(Constant.ADMIN_ROLE)){
                role = RoleConstant.ADMIN;
                break;
            }
        }

        // Update school status
        if (schoolInfoService.updateSchoolStatusByRequest(id, recordNo, newStatus, role, inStatus) > 0) {
            if(mailId != null && detail != null && toMail != null && ccMail != null){
                // Send email to school owner
                emailService.sendEmailToMany(SendMailInfo.builder().toMail(toMail).ccMail(ccMail).mailId(mailId).detail(detail).build());

            }
            return ResponseEntity.ok(Map.of("message", "Successfully!"));
        } else return ResponseEntity.badRequest().body(Map.of("message", "Failed!"));
    }

    private void sendEmailForSubmitted(int schoolId) {
        List<String> sendTo = accountService.getAllAccountEmailsByRole(RoleConstant.ADMIN_ROLE.getKey());
        Map<Placeholder, String> details = new HashMap<Placeholder, String>();
        details.put(Placeholder.TITLE, "Review Submitted");
        details.put(Placeholder.LINK, globalConfig.getServerLink() + "/manager/school/view-detail?id=" + schoolId);
        emailService.sendEmailToMany(SendMailInfo.builder().toMail(sendTo).ccMail(List.of()).detail(details).build());
    }

    private String saveImage(MultipartFile image, int schoolId) throws IOException {
        if(image != null && !image.isEmpty()){

            File uploadFolder = new File(Constant.IMAGE_DIR);
            if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
                throw new IOException("Failed to create directory: " + Constant.IMAGE_DIR);
            }

            String fileName = System.currentTimeMillis() + "_" + schoolId;
            Path filePath = Paths.get(Constant.IMAGE_DIR).resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return Constant.IMAGE_DIR + "/" + fileName;
        }
        return null;
    }
}
