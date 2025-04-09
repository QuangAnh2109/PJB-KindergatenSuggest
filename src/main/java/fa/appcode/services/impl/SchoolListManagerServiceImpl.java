package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.RoleConstant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.common.utils.SchoolFormButton;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.config.GlobalConfig;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolInfoService;
import fa.appcode.services.SchoolListManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class SchoolListManagerServiceImpl implements SchoolListManagerService {

    private final MasterDatumService masterDatumService;

    private final SchoolInfoService schoolInfoService;

    private final GlobalConfig globalConfig;

    @Override
    public String setSchoolDataToModelBySearchAndPage(String search, int pageNumber, boolean ajaxCall, Model model) throws NullPointerException, DataAccessException {
        // Get the current user's authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is an admin
        authentication.getAuthorities().forEach(authority -> {
            if(authority.getAuthority().equals(Constant.ADMIN_ROLE)){
                model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndPagingAndDeleteFlg(pageNumber, search));
            }else{
                model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndAccountAndPagingAndDeleteFlg(pageNumber, search, authentication.getName()));
            }
        });

        // Add attributes to the model
        model.addAttribute("status", masterDatumService.findAllByTypeNameNoDelete(SchoolConstant.SCHOOL_STATUS));
        model.addAttribute("currentPage", pageNumber);

        // Return the page link
        if(ajaxCall) return Constant.SCHOOL_LIST_MANAGER_PAGE + " :: main-content";
        else return Constant.SCHOOL_LIST_MANAGER_PAGE;
    }
}
