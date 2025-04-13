package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.TokenUtils;
import fa.appcode.common.utils.ValidateUtils;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.SchoolInfo;
import fa.appcode.exceptions.ValidationException;
import fa.appcode.services.EntityValidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntityValidateServiceImpl implements EntityValidateService {
    private final GlobalConfig globalConfig;

    private final Logger logger = LoggerFactory.getLogger(EntityValidateServiceImpl.class);

    private void validateSchoolName(String schoolName, Map<String, String> errors) {
        ValidateUtils.validateString(schoolName, Constant.SCHOOL_NAME_REGEX, Constant.NAME_MESSAGE_KEY, globalConfig.getSchoolNameNotNull(), globalConfig.getSchoolNameTooLong(), errors);
    }

    private void validateAddress(String address, Map<String, String> errors) {
        ValidateUtils.validateString(address, Constant.ADDRESS_REGEX, Constant.ADDRESS_MESSAGE_KEY, globalConfig.getAddressNotNull(), globalConfig.getAddressTooLong(), errors);
    }

    private void validateEmail(String email, Map<String, String> errors) {
        ValidateUtils.validateString(email, Constant.EMAIL_REGEX, Constant.EMAIL_MESSAGE_KEY, globalConfig.getEmailNotNull(), globalConfig.getInValidEmail(), errors);
    }

    private void validatePhone(String phone, Map<String, String> errors) {
        ValidateUtils.validateString(phone, Constant.PHONE_REGEX, Constant.PHONE_MESSAGE_KEY, globalConfig.getPhoneNumberNotNull(), globalConfig.getPhoneIsNotValid(), errors);
    }

    private void validateFeeFromTo(BigDecimal feeFrom, BigDecimal feeTo, Map<String, String> errors) {
        ValidateUtils.validateFee(feeFrom, Constant.FEE_FROM_MESSAGE_KEY, errors, globalConfig.getFeeFromNotNull(), globalConfig.getFeeFromExceedsLimit(), globalConfig.getFeeFromNegative());
        ValidateUtils.validateFee(feeTo, Constant.FEE_TO_MESSAGE_KEY, errors, globalConfig.getFeeToNotNull(), globalConfig.getFeeToExceedsLimit(), globalConfig.getFeeToNegative());
        if(feeFrom != null && feeTo != null && feeFrom.compareTo(feeTo) > 0) {
            errors.put(Constant.FEE_MESSAGE_KEY, globalConfig.getFeeFromLessThanFeeTo());
        }
    }

    private void validateSchoolIntroduction(String introduction, Map<String, String> errors) {
        ValidateUtils.validateString(introduction, Constant.SCHOOL_INTRODUCTION_REGEX, Constant.INTRODUCTION_MESSAGE_KEY, globalConfig.getSchoolIntroTooLong(), errors);
    }

    private void validateSchoolImage(MultipartFile image, Map<String, String> errors) {
        if (image != null && !image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            String contentType = image.getContentType();
            if (fileName == null || contentType == null || !fileName.toLowerCase().endsWith(".png") || !contentType.equals("image/png")) {
                errors.put(Constant.IMAGE_MESSAGE_KEY,globalConfig.getImageMustBePng());
            }
        }
    }

    @Override
    public void validateSchoolSearchForManager(String search, Map<String, String> errors) {

    }

    @Override
    public void validateRatingFeedbackDateFromTo(Instant dateFrom, Instant dateTo) throws ValidationException{
        Map<String, String> errors = new HashMap<>() ;
        Instant now = Instant.now();
        if (dateTo != null && dateTo.compareTo(now) >= 0) {
            errors.put(Constant.DATE_TO_MESSAGE_KEY, globalConfig.getInvalidToDate());
        }
        if (dateFrom != null && dateFrom.compareTo(now) >= 0) {
            errors.put(Constant.DATE_FROM_MESSAGE_KEY, globalConfig.getInvalidFromDate());
        }
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            errors.put(Constant.DATE_MESSAGE_KEY, globalConfig.getFromDateGreaterThanToDate());
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    /**
     * @param schoolInfo
     * @param image
     * @throws ValidationException
     */
    @Override
    public void validateUpdateSchool(SchoolInfo schoolInfo, MultipartFile image) throws ValidationException {
        Map<String, String> errors = new HashMap<>();
        validateSchoolName(schoolInfo.getSchoolName(), errors);
        validateAddress(schoolInfo.getSchoolAddress(), errors);
        validateEmail(schoolInfo.getSchoolEmail(), errors);
        validatePhone(schoolInfo.getSchoolPhone(), errors);
        validateFeeFromTo(schoolInfo.getFeeFrom(), schoolInfo.getFeeTo(), errors);
        validateSchoolIntroduction(schoolInfo.getSchoolIntroduction(), errors);
        validateSchoolImage(image, errors);
        ValidateUtils.validateObjectNull(schoolInfo.getTypeId(), Constant.SCHOOL_TYPE_MESSAGE_KEY, globalConfig.getSchoolTypeNotNull(), errors);
        ValidateUtils.validateObjectNull(schoolInfo.getCity(), Constant.CITY_MESSAGE_KEY, globalConfig.getCityNotNull(), errors);
        ValidateUtils.validateObjectNull(schoolInfo.getDistrict(), Constant.DISTRICT_MESSAGE_KEY, globalConfig.getDistrictNotNull(), errors);
        ValidateUtils.validateObjectNull(schoolInfo.getWard(), Constant.WARD_MESSAGE_KEY, globalConfig.getWardNotNull(), errors);
        ValidateUtils.validateObjectNull(schoolInfo.getChildReceivingAgeId(), Constant.CHILD_RECEIVING_AGE_MESSAGE_KEY, globalConfig.getChildReceivingAgeNotNull(), errors);
        ValidateUtils.validateObjectNull(schoolInfo.getEducationMethodId(), Constant.EDUCATION_METHOD_MESSAGE_KEY, globalConfig.getEducationMethodNotNull(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    /**
     * @param decoded
     * @return
     */
    @Override
    public boolean validateViewSchoolDetailToken(String[] decoded) {
        if(decoded != null && decoded.length == 2) {
            try{
                Integer.parseInt(decoded[0]);
                Integer.parseInt(decoded[1]);
                return true;
            }
            catch(NumberFormatException e){
                logger.info("Invalid view school detail token {}", e.getMessage());
            }
        }
        return false;
    }

}
