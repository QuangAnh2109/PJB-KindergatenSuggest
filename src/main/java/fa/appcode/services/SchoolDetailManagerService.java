package fa.appcode.services;

import org.springframework.ui.Model;

public interface SchoolDetailManagerService {
    public void setSchoolUpdateFormToModel(Model model, boolean edit, String title1, String title2);
}
