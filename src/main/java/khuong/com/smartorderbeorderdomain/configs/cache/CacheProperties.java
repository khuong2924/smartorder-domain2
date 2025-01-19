package khuong.com.smartorderbeorderdomain.configs.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.cache")
@Data
public class CacheProperties {
    private long timeoutMinutes = 60;
    private long maxSize = 100;
}
