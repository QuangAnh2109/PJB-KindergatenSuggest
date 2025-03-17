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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public ResponseEntity<String> createNewSchool(SchoolInfo schoolInfo, MultipartFile image, List<Integer> schoolFacilities, List<Integer> schoolUtilities) throws DataAccessException {

        try {
            // Get current account
            String uploadDir = "src/main/resources/static/admin_side/images";
            String imagePath = "default.png";

            if (image != null && !image.isEmpty()) {
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
                    throw new IOException("Failed to create directory: " + uploadDir);
                }

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = fileName; // Save file name to DB
            }

            // save to DB
            schoolInfo.setImageUrl(imagePath);
            schoolInfoRepository.save(schoolInfo);
            return ResponseEntity.ok().body("School saved successfully!");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save school due to file upload error.", e);
        }
    }

    @Override
    public ResponseEntity<String> changeSchoolStatus(int id, int recordNo, int newStatus, List<Integer> inStatus, Integer mailId, Map<Placeholder, String> detail, List<String> toMail, List<String> ccMail) throws DataAccessException {
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
            return ResponseEntity.accepted().body("Submit success");
        } else return ResponseEntity.badRequest().body("Submit failed");
    }
}
