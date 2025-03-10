package fa.appcode.services.impl;

import fa.appcode.common.vo.EmailContentVo;
import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import fa.appcode.repositories.RequestRepository;
import fa.appcode.services.EmailService;
import fa.appcode.services.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Override
    public Page<RequestVo> listAllRequest(Integer accountID,Integer requestMasterID,Pageable pageable) {
        return (Page<RequestVo>) requestRepository.listAllRequest(accountID,requestMasterID,pageable);
    }

    @Override
    public RequestDetailVo findById(Integer id) {
        return requestRepository.findRequestsById(id);
    }

    @Override
    public Page<RequestVo> searchRequest(String keyword, Integer accountID, Integer requestMasterID, Pageable pageable) {
        return (Page<RequestVo>)requestRepository.searchRequest(keyword,accountID,requestMasterID,pageable);
    }


    @Override
    public Page<RequestDetailVo> findRequestByAccountId(Integer accountId, Pageable pageable) {
        logger.info("Fetching requests by accountId and deleteFlg");
        try{
            Page<RequestDetailVo> result = requestRepository.findRequestByAccountId(accountId,pageable);
            logger.info("Found {} requests for account ID: {}", result.getTotalElements(), accountId);
            return result;
        }catch (DataAccessException e){
            logger.error("Database error when fetching requests for account ID: {}", accountId, e);
            return Page.empty();
        }
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

    @Override
    public void createRequest(Request request) {
        requestRepository.save(request);
    }
}
