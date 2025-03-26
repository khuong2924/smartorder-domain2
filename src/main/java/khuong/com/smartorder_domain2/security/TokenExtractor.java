package khuong.com.smartorder_domain2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenExtractor {

    @Autowired
    private JwtUtil jwtUtil;

    public String extractUserId(String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token != null) {
            return jwtUtil.extractUserId(token);
        }
        return null;
    }

    private String extractTokenFromHeader(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}