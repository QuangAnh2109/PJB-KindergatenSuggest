package fa.appcode.services.impl;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.AccountFeedback;
import fa.appcode.common.vo.SchoolFormManager;
import fa.appcode.common.vo.SchoolRatingFeedback;
import fa.appcode.common.vo.SchoolRatingFeedbackForm;
import fa.appcode.config.GlobalConfig;
import fa.appcode.repositories.SchoolInfoRepository;
import fa.appcode.services.FeedbackRatingService;
import fa.appcode.services.SchoolInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public void setBaseData(Model model, int schoolId, String accountEmail) {
        SchoolRatingFeedback test = schoolInfoRepository.getSchoolRatingFeedbackBySchoolId(SchoolRatingFeedbackForm.builder().schoolId(schoolId).accountEmail(accountEmail).build());
        if(test == null) test = new SchoolRatingFeedback(schoolId);
        model.addAttribute("rating",test);
        model.addAttribute("feedbackList", schoolInfoRepository.getAllAccountFeedbackBySchoolId(SchoolRatingFeedbackForm.builder().schoolId(schoolId).accountEmail(accountEmail).build(), PageRequest.of(Constant.PAGE_DEFAULT, globalConfig.getSizeOfPageSchoolRatingFeedback())));
    }
}
