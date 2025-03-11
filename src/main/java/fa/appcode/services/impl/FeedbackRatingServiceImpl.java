package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.SchoolRatingFeedbackForm;
import fa.appcode.config.GlobalConfig;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.FeedbackRatingService;
import fa.appcode.services.SchoolInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class FeedbackRatingServiceImpl implements FeedbackRatingService {

    private final SchoolInfoRepository schoolInfoRepository;

    private final GlobalConfig globalConfig;

    @Override
    public void setBaseData(Model model, int schoolId, int accountId) {
        model.addAttribute("rating", schoolInfoRepository.getSchoolRatingFeedbackBySchoolId(SchoolRatingFeedbackForm.builder().schoolId(schoolId).accountId(accountId).build()));
        model.addAttribute("feedbackList", schoolInfoRepository.getAllAccountFeedbackBySchoolId(SchoolRatingFeedbackForm.builder().schoolId(schoolId).accountId(accountId).build(), PageRequest.of(Constant.PAGE_DEFAULT, globalConfig.getSizeOfPageSchoolRatingFeedback())));
        if(!schoolInfoRepository.getAllAccountFeedbackBySchoolId(SchoolRatingFeedbackForm.builder().schoolId(schoolId).accountId(accountId).build(), PageRequest.of(Constant.PAGE_DEFAULT+1, globalConfig.getSizeOfPageSchoolRatingFeedback())).isEmpty()){
            model.addAttribute("hasNext", true);
            model.addAttribute("nextPage", Constant.PAGE_DEFAULT+1);
        } else{
            model.addAttribute("hasNext", false);
        }
    }
}
