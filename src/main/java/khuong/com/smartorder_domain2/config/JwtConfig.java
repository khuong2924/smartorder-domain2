package khuong.com.smartorder_domain2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

@Configuration
public class JwtConfig {
    
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Bean
    public Key jwtSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}