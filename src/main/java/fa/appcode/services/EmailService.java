package fa.appcode.services;

import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.Map;

public interface EmailService {
    void sendEmailToMany(SendMailInfo sendMailInfo);

    void sendEmail(String to, String subject, String body);
}
