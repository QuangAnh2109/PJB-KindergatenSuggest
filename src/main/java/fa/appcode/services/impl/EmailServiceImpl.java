package fa.appcode.services.impl;

import fa.appcode.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Async
    @Override
    public void sendEmail(String toEmail, String subject, String text) {
        log.info("Sending email to {} on thread: {}", toEmail, Thread.currentThread().getName());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
            log.info(" Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }



}
