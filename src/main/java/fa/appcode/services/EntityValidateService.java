package fa.appcode.services;

import fa.appcode.entities.SchoolInfo;
import fa.appcode.exceptions.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;

public interface EntityValidateService {
    void validateSchoolSearchForManager(String search, Map<String, String> errors);
    void validateRatingFeedbackDateFromTo(Instant dateFrom, Instant dateTo, Map<String, String> errors);
    void validateUpdateSchool(SchoolInfo schoolInfo, MultipartFile image) throws ValidationException;
}