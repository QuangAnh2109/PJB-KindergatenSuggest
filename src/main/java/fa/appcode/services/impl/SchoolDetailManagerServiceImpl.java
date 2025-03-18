package fa.appcode.services.impl;

import fa.appcode.common.utils.*;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.*;
import fa.appcode.repositories.*;
import fa.appcode.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.Role;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final SchoolUtilityRepository schoolUtilityRepository;

    private final SchoolFacilityRepository schoolFacilityRepository;

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

        try {
            if(image != null && !image.isEmpty()){
                // Get current account
                String uploadDir = Constant.IMAGE_DIR;
                String imagePath;

                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
                    throw new IOException("Failed to create directory: " + uploadDir);
                }

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = fileName; // Save file name to DB

                // save to DB
                schoolInfo.setImageUrl(imagePath);
            }

            schoolInfoRepository.save(schoolInfo);
            schoolUtilityRepository.saveAll(schoolUtilityId.stream()
                    .map(utilityId -> {
                        SchoolUtility su = new SchoolUtility();
                        su.setId(new SchoolUtilityId(schoolInfo.getId(), utilityId));
                        su.setSchool(schoolInfo);
                        su.setCreateId(RoleConstant.SCHOOL_OWNER);
                        su.setCreateTime(Instant.now());
                        su.setUpdateId(RoleConstant.SCHOOL_OWNER);
                        su.setUpdateTime(Instant.now());
                        su.setDeleteFlg(false);
                        su.setRecordNo(1);
                        return su;
                    })
                    .collect(Collectors.toList())
            );

            schoolFacilityRepository.saveAll(schoolFacilityId.stream()
                    .map(facilityId -> {
                        SchoolFacility sf = new SchoolFacility();
                        sf.setId(new SchoolFacilityId(schoolInfo.getId(), facilityId));
                        sf.setSchool(schoolInfo);
                        sf.setCreateId(RoleConstant.SCHOOL_OWNER);
                        sf.setCreateTime(Instant.now());
                        sf.setUpdateId(RoleConstant.SCHOOL_OWNER);
                        sf.setUpdateTime(Instant.now());
                        sf.setDeleteFlg(false);
                        sf.setRecordNo(1);
                        return sf;
                    })
                    .collect(Collectors.toList())
            );
            return ResponseEntity.ok().body(Map.of("message", "Create school successfully!", "id", schoolInfo.getId()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save school due to file upload error.", e);
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
            if(image != null && !image.isEmpty()){
                // Get current account
                String uploadDir = Constant.IMAGE_DIR;
                String imagePath;

                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
                    throw new IOException("Failed to create directory: " + uploadDir);
                }

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = fileName; // Save file name to DB

                // save to DB
                schoolInfo.setImgageUrl(imagePath);
            }

            schoolInfoService.updateSchoolInfoBySchoolFormManager(schoolInfo);
            schoolUtilityRepository.saveAll(schoolUtilityId.stream()
                    .map(utilityId -> {
                        SchoolUtility su = new SchoolUtility();
                        su.setId(new SchoolUtilityId(schoolInfo.getId(), utilityId));
                        su.setSchool(schoolInfoDb);
                        su.setCreateId(RoleConstant.SCHOOL_OWNER);
                        su.setCreateTime(Instant.now());
                        su.setUpdateId(RoleConstant.SCHOOL_OWNER);
                        su.setUpdateTime(Instant.now());
                        su.setDeleteFlg(false);
                        su.setRecordNo(1);
                        return su;
                    })
                    .collect(Collectors.toList())
            );

            schoolFacilityRepository.saveAll(schoolFacilityId.stream()
                    .map(facilityId -> {
                        SchoolFacility sf = new SchoolFacility();
                        sf.setId(new SchoolFacilityId(schoolInfo.getId(), facilityId));
                        sf.setSchool(schoolInfoDb);
                        sf.setCreateId(RoleConstant.SCHOOL_OWNER);
                        sf.setCreateTime(Instant.now());
                        sf.setUpdateId(RoleConstant.SCHOOL_OWNER);
                        sf.setUpdateTime(Instant.now());
                        sf.setDeleteFlg(false);
                        sf.setRecordNo(1);
                        return sf;
                    })
                    .collect(Collectors.toList())
            );
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
                // Get school owner email
                String email = accountService.getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(id);

                // Send email to school owner
                emailService.sendEmailToMany(SendMailInfo.builder().toMail(toMail).ccMail(ccMail).mailId(mailId).detail(detail).build());
            }
            return ResponseEntity.ok(Map.of("message", "Successfully!"));
        } else return ResponseEntity.badRequest().body(Map.of("message", "Failed!"));
    }
}
