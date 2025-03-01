package fa.appcode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:email.properties")
@Getter
@Component
public class EmailConfig {
    @Value("${REGEX_MAIL_TEXT_PLACEHOLDER}")
    public String REGEX_MAIL_TEXT_PLACEHOLDER;

    @Value("${spring.mail.username}")
    private String SYSTEM_MAIL;

    @Value("${mail.to.manager}")
    private String MANAGER_MAIL;
}
