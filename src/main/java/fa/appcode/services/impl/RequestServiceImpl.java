package fa.appcode.services.impl;

import com.cloudinary.provisioning.Account;
import fa.appcode.common.utils.Constant;
import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.common.vo.EmailContentVo;
import fa.appcode.common.vo.MyRequestVo;
import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.config.GlobalConfig;
import fa.appcode.entities.AccountInfo;
import fa.appcode.entities.Request;
import fa.appcode.repositories.AccountRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public Page<RequestVo> listAllRequest(Integer accountID, Integer requestMasterID, Pageable pageable) {
        return (Page<RequestVo>) requestRepository.listAllRequest(accountID, requestMasterID, pageable);
    }

    @Override
    public RequestDetailVo findById(Integer id) {
        return requestRepository.findRequestsById(id);
    }

    @Override
    public Page<RequestVo> searchRequest(String keyword, Integer accountID, Integer requestMasterID, Pageable pageable) {
        return (Page<RequestVo>) requestRepository.searchRequest(keyword, accountID, requestMasterID, pageable);
    }


    @Override
    public Page<MyRequestVo> findRequestByAccountId(Integer accountId, Pageable pageable) throws DataAccessException{
        logger.info("Fetching requests by accountId and deleteFlg");
        Page<MyRequestVo> result = requestRepository.findRequestByAccountId(accountId,pageable);
        logger.info("Found {} requests for account ID: {}", result.getTotalElements(), accountId);
        return result;
    }

    @Override
    public String updateRequest(Integer id , Integer recordNo, AccountInfo account) {
        Request currRequest = requestRepository.findRequestsInformationById(id);
        if(recordNo!=currRequest.getRecordNo()){
            return globalConfig.getUpdateFailMessage();
        }
        currRequest.setRecordNo(currRequest.getRecordNo()+1);
        currRequest.setUpdateTime(Instant.now());
        String role = account.getRoleId()==1?Constant.ADMIN_ROLE:Constant.SCHOOL_OWNER_ROLE;
        if(role.equalsIgnoreCase(Constant.ADMIN_ROLE)){
            currRequest.setUpdateId("SYSTEM_ADMIN");
        }else{
            currRequest.setUpdateId("SCHOOL_OWNER");
        }
        currRequest.setRequestMasterId(2);
        requestRepository.save(currRequest);
        return globalConfig.getUpdateSuccessfullMessage();
    }

    @Override
    @Scheduled(cron = "0 0 0 */2 * ?")
    //@Scheduled(cron = "*/5 * * * * ?")
    public void emailRequestReminder() {
        List<EmailContentVo> listSending = requestRepository.findAccountForEmail();
        List<String> emailList = new ArrayList<>();
        //emailList.add("dongquang569@gmail.com");
        for (EmailContentVo email : listSending) {
            if (email.getNumberOfRequest() > 0 || email.getRoleID() == 1) {
                //System.out.println(email.getEmail());
                emailList.add(email.getEmail());
            }
        }
        String reminderLink = Constant.REQUEST_REMINDER_URL;
        Map<Placeholder, String> link = Map.of(Placeholder.LINK, reminderLink);
        SendMailInfo sendMailInfo = SendMailInfo.builder()
                .toMail(emailList)
                .mailId(Constant.SEND_REQUEST_REMINDER)
                .ccMail(List.of())
                .detail(link)
                .build();
        emailService.sendEmailToMany(sendMailInfo);
    }

    @Override
    public void createRequest(Request request) {
        requestRepository.save(request);
    }
}
