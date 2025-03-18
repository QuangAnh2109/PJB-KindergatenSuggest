package fa.appcode.services;

import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;

public interface SchoolListManagerService {
    /**
     *
     * @param search
     * @param pageNumber
     * @param ajaxCall
     * @param model
     * @return
     * @throws NullPointerException
     * @throws DataAccessException
     */
    String setSchoolDataToModelBySearchAndPage(String search, int pageNumber, boolean ajaxCall, Model model) throws NullPointerException, DataAccessException;
}
