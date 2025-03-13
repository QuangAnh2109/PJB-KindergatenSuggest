package fa.appcode.services;

import org.springframework.ui.Model;

public interface FeedbackRatingService {
    public void setBaseData(Model model, int schoolId, String accountEmail);
}
