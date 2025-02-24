package fa.appcode.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:webconfig.properties")
@ConfigurationProperties
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalConfig {
    @Value("${page.init}")
    private Integer initPage;
    @Value("${ME_001}")
    private String incorrectLogin;
    @Value("${ME_002}")
    private String passwordValidate;
    @Value("${ME_003}")
    private String requiredField;
    @Value("${ME_004}")
    private String sendResetPassword;
    @Value("${ME_005}")
    private String expiredLink;
    @Value("${ME_006}")
    private String emailNotExist;
    @Value("${ME_007}")
    private String passwordNotMatch;
    @Value("${ME_008}")
    private String validatePassword;
    @Value("${ME_009}")
    private String nullMessage;
    @Value("${ME_010}")
    private String saveSchoolSuccess;
    @Value("${ME_011}")
    private String submitSchool;
    @Value("${ME_012}")
    private String notFound;
    @Value("${ME_013}")
    private String enrollSuccess;
    @Value("${ME_014}")
    private String emailExist;

    @Value("${page.size}")
    private Integer sizeOfPage;
}
