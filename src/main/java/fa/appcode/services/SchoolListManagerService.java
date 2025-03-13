package fa.appcode.services;

import org.springframework.ui.Model;

public interface SchoolListManagerService {
    /**
     *
     * @param search
     * @param page
     * @param isAdmin
     * @param ajaxCall
     * @return page link
     */
    public String setSchoolDataToModelBySearchAndPage(String search, int page, boolean isAdmin, boolean ajaxCall, Model model);
}
