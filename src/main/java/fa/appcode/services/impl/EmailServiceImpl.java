package fa.appcode.services.impl;

import fa.appcode.common.vo.MasterMailVo;
import fa.appcode.services.EmailService;
import fa.appcode.services.MasterMailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

@PropertySource("application.properties")
@Configuration
@EnableAsync
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MasterMailService masterMailService;

    @Value("${spring.mail.username}")
    private String systemMail;

    @Value("${mail.to.manager}")
    private String managerMail;

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Async
    @Override
    public void sendEmail(String toEmail, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(systemMail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
            log.info(" Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }

    @Async
    @Override
    public void sendEmailToOne(String mail, Integer id, Map<String, Object> detail) {
        try {
            sendMail(systemMail, new String[]{mail}, new String[]{managerMail}, id, detail);
            log.info(" Email sent successfully to {}", mail);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }

    @Async
    @Override
    public void sendEmailToMany(String[] mail, Integer id, Map<String, Object> detail) {
        try {
            sendMail(systemMail, mail, new String[]{managerMail}, id, detail);
            log.info(" Email sent successfully to {}", Arrays.toString(mail));
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }

    private void sendMail(String fromMail, String[] toMail, String[] ccMail, Integer id, Map<String, Object> detail) throws Exception{
        //get master mail
        MasterMailVo masterMailVo = masterMailService.findByIdAndNoDelete(id);
        //declare message support html
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        //set the sender, to, cc email address
        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setTo(toMail);
        mimeMessageHelper.setCc(ccMail);
        //set subject and text
        mimeMessageHelper.setSubject(replaceMapString(masterMailVo.getSubject(), detail));
        mimeMessageHelper.setText(replaceMapString(masterMailVo.getTitle(), detail), true);
        //send message
        javaMailSender.send(message);
    }

    private String replaceMapString(String content, Map<String, Object> detail){
        for (Map.Entry<String, Object> entry : detail.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue().toString());
        }
        return content;
    }
}
