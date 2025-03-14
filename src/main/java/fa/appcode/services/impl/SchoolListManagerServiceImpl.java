package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.config.GlobalConfig;
import fa.appcode.repositories.MasterDatumRepository;
import fa.appcode.services.MasterDatumService;
import fa.appcode.services.SchoolInfoService;
import fa.appcode.services.SchoolListManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class SchoolListManagerServiceImpl implements SchoolListManagerService {

    private final MasterDatumService masterDatumService;

    private final SchoolInfoService schoolInfoService;

    private final GlobalConfig globalConfig;

    @Override
    public String setSchoolDataToModelBySearchAndPage(String search, int page, boolean isAdmin, boolean ajaxCall, Model model) {
        if(isAdmin){
            model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndPagingAndDeleteFlg(page, search));
        }else{
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("schoolList", schoolInfoService.searchAllByNameAndAccountAndPagingAndDeleteFlg(Constant.PAGE_DEFAULT, Constant.SEARCH_ALL, email));
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("status", masterDatumService.findAllByTypeNameNoDelete(SchoolConstant.SCHOOL_STATUS));
        model.addAttribute("currentPage", page);
        model.addAttribute("serverLink", globalConfig.getServerLink());
        if(ajaxCall) return Constant.SCHOOL_LIST_MANAGER_PAGE + " :: main-content";
        else return Constant.SCHOOL_LIST_MANAGER_PAGE;
    }
}
