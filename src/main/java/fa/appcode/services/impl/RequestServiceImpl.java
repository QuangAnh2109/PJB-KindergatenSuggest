package fa.appcode.services.impl;

import fa.appcode.common.vo.EmailContentVo;
import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import fa.appcode.repositories.RequestRepository;
import fa.appcode.services.EmailService;
import fa.appcode.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private EmailService emailService;


    @Override
    public Page<RequestVo> findAll(Pageable pageable) {
        return (Page<RequestVo>) requestRepository.listAllRequest(pageable);
    }

    @Override
    public Page<RequestVo> listAllRequestWithSchoolOwner(Integer accountID, Pageable pageable) {
        return (Page<RequestVo>) requestRepository.listAllRequestWithSchoolOwner(accountID, pageable);
    }

    @Override
    public RequestDetailVo findById(Integer id) {
        return requestRepository.findRequestsById(id);
    }

    @Override
    public Page<RequestVo> findOpenedRequest(Pageable pageable) {
        return (Page<RequestVo>) requestRepository.findOpenedRequest(pageable);
    }

    @Override
    public Page<RequestVo> findOpenedRequestWithSchoolOwner(Integer accountID, Pageable pageable) {
        return (Page<RequestVo>) requestRepository.findOpenedRequestWithSchoolOwner(accountID, pageable);
    }

    @Override
    public Page<RequestVo> searchRequest(String keyword, Pageable pageable) {
        return (Page<RequestVo>) requestRepository.searchRequest(keyword, pageable);
    }

    @Override
    public Page<RequestVo> searchRequestWithSchoolOwner(String keyword, Integer accountID, Pageable pageable) {
        return (Page<RequestVo>) requestRepository.searchRequestWithSchoolOwner(keyword, accountID, pageable);
    }

    @Override
    public Page<RequestVo> searchRequestReminder(String keyword, Pageable pageable) {
        return (Page<RequestVo>) requestRepository.searchRequestReminder(keyword, pageable);
    }

    @Override
    public Page<RequestVo> searchRequestReminderWithSchoolOwner(String keyword, Integer accountID, Pageable pageable) {
        return (Page<RequestVo>) requestRepository.searchRequestReminderWithSchoolOwner(keyword, accountID, pageable);
    }

    @Override
    public Page<Request> findRequestByAccountIdAndDeleteFlg(Integer accountId) {
        return (Page<Request>) requestRepository.findRequestByAccountIdAndDeleteFlgIsFalse(accountId);
    }

    @Override
    public void updateRequest(String update_id, int id) {
        Instant vietnamTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
        requestRepository.updateRequestStatus(update_id, id, vietnamTime);
    }

    @Override
    @Scheduled(cron = "0 0 0 */2 * ?")
    public void emailRequestReminder() {
        List<EmailContentVo> listSending = requestRepository.findAccountForEmail();
        for (EmailContentVo emailContentVo : listSending) {
            emailService.sendEmail(emailContentVo.getEmail(), "Kindergarten",
                    "You have " + emailContentVo.getNumberOfRequest() + " unresolved requests");
        }
    }
}
