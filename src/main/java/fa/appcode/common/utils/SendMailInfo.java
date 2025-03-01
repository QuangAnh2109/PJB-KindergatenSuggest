package fa.appcode.common.utils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class SendMailInfo {
    @Value("${spring.mail.username}")
    String fromMail;

    @NotEmpty
    List<@Email @NotEmpty String> toMail;

    @NotNull
    List<@Email @NotEmpty String> ccMail;

    @NotNull
    Integer mailId;

    @NotNull
    Map<Placeholder, String> detail;
}
