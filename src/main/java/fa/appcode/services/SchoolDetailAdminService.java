package fa.appcode.services;

import org.springframework.ui.Model;

public interface SchoolDetailAdminService {
    public boolean deleteSchoolByStatus(int id, int record);
    public boolean setButtonSchoolDetailFormToModel(Model model, int id, boolean edit);
}