package fa.appcode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:webconfig.properties")
@ConfigurationProperties
@Getter
public class GlobalConfig {
    @Value("${page.init}")
    private Integer initPage;

    @Value("${page.size}")
    private Integer sizeOfPage;
}
