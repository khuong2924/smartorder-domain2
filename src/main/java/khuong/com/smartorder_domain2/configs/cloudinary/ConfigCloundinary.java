package khuong.com.smartorder_domain2.configs.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloundinary {

    @Bean
    public Cloudinary configKey() {
        try {


            Map<String, String> config = new HashMap<>();

            config.put("cloud_name", "decz34g1a");
            config.put("api_key", "325126569821533");
            config.put("api_secret", "wqcbw8JTOaND-yfguO_6p8NhwHc");
            return new Cloudinary(config);
        } catch (Exception e) {
            throw new RuntimeException("Cloudinary configuration error", e);
        }
    }
}
