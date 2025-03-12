package fa.appcode.services;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface SchoolDetailManagerService {
    public void setSchoolUpdateFormToModel(Model model);
    public void setSchoolViewDetailFormToModel(Model model, int cityId, int districtId);
}