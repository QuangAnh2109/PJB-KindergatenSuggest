package fa.appcode.services.impl;

import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.common.vo.MasterMailVo;
import fa.appcode.config.EmailConfig;
import fa.appcode.exceptions.LackPlaceholderException;
import fa.appcode.services.EmailService;
import fa.appcode.services.MasterMailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender javaMailSender;

    private final MasterMailService masterMailService;

    private final EmailConfig emailConfig;

    private final Validator validator;

    @Override
    public boolean sendEmailToMany(SendMailInfo sendMailInfo) {
        try {
            //check if sendMailInfo is valid
            validate(sendMailInfo);

            //get master mail
            MasterMailVo masterMailVo = masterMailService.findByIdAndNoDelete(sendMailInfo.getMailId());

            //declare message support html
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

            //set the sender, to, cc email address
            mimeMessageHelper.setFrom(emailConfig.getSYSTEM_MAIL());
            mimeMessageHelper.setTo(sendMailInfo.getToMail().toArray(new String[0]));
            mimeMessageHelper.setCc(sendMailInfo.getCcMail().toArray(new String[0]));
            mimeMessageHelper.addCc(emailConfig.getMANAGER_MAIL());

            //set subject and text
            mimeMessageHelper.setSubject(replaceMapString(masterMailVo.getSubject(), sendMailInfo.getDetail(), "subject"));
            mimeMessageHelper.setText(replaceMapString(masterMailVo.getTitle(), sendMailInfo.getDetail(), "text"), true);

            //send message
            javaMailSender.send(message);
            log.info("Email sent successfully to {}", sendMailInfo.getToMail());
            return true;
        } catch (ConstraintViolationException e) {
            log.error("SendMailInfo is not valid: {}", e.getMessage());
        } catch (NullPointerException e) {
            log.error("Mail not found: {}", e.getMessage());
        } catch (LackPlaceholderException e) {
            log.error("Missing placeholder: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
        return false;
    }

    private void validate(SendMailInfo sendMailInfo) throws ConstraintViolationException{
        //validate sendMailInfo
        Set<ConstraintViolation<SendMailInfo>> violations = validator.validate(sendMailInfo);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private String replaceMapString(String content, Map<Placeholder, String> detail, String type) throws Exception {
        //declare placeholder and LackPlaceholderException message
        Placeholder placeholder;
        StringBuilder message = new StringBuilder();

        //find all placeholder in content
        Matcher matcher = Pattern.compile(emailConfig.getREGEX_MAIL_TEXT_PLACEHOLDER()).matcher(content);

        //replace all placeholder with value in detail
        while(matcher.find()){

            //get placeholder
            placeholder = Placeholder.valueOf(matcher.group(1));

            //replace placeholder with value
            if(detail.containsKey(placeholder)){
                content = content.replace(placeholder.getPlaceholder(), detail.get(placeholder));
            }
            else message.append(", ").append(placeholder);
        }

        if(message.isEmpty()){
            return content;
        }
        else{
            throw new LackPlaceholderException(message.substring(2)+ "("+type+")");
        }
    }
}
