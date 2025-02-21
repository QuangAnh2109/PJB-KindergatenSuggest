package fa.appcode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:webconfig.properties")
@ConfigurationProperties
@Getter
public class GlobalConfig {
    @Value("1")
    private Integer initPage;





}

