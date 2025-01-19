package khuong.com.smartorderbeorderdomain.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.cache.caffeine.spec}")
    private String caffeineSpec;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeineSpec(CaffeineSpec.parse(caffeineSpec));
        cacheManager.setCacheNames(Arrays.asList(
                CacheConstants.CATEGORY_CACHE,
                CacheConstants.MENU_ITEM_CACHE,
                CacheConstants.MENU_OPTION_CACHE
        ));
        return cacheManager;
    }
}