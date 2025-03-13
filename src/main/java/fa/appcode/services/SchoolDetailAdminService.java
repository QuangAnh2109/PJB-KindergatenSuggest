package fa.appcode.services;

import org.springframework.ui.Model;

public interface SchoolDetailAdminService {
    public boolean deleteSchoolByStatus(int id, int recordNo);
    public String getSchoolDetail(Model model, int schoolId);
}