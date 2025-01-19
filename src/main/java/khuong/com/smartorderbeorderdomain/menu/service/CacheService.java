package khuong.com.smartorderbeorderdomain.menu.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String MENU_ITEM_KEY_PREFIX = "menu:item:";
    private static final String MENU_CATEGORY_KEY_PREFIX = "menu:category:";
    private static final String MENU_SPECIAL_KEY_PREFIX = "menu:special:";

    public void cacheMenuItem(MenuItemDTO menuItem) {
        String key = MENU_ITEM_KEY_PREFIX + menuItem.getId();
        redisTemplate.opsForValue().set(key, menuItem, Duration.ofHours(24));
        log.debug("Cached menu item: {}", key);
    }

    public Optional<MenuItemDTO> getCachedMenuItem(Long id) {
        String key = MENU_ITEM_KEY_PREFIX + id;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.debug("Cache hit for menu item: {}", key);
            return Optional.of((MenuItemDTO) cached);
        }
        log.debug("Cache miss for menu item: {}", key);
        return Optional.empty();
    }

    public void invalidateMenuItem(Long id) {
        String key = MENU_ITEM_KEY_PREFIX + id;
        redisTemplate.delete(key);
        log.debug("Invalidated cache for menu item: {}", key);
    }

    public void cacheMenuCategory(MenuCategoryDTO category) {
        String key = MENU_CATEGORY_KEY_PREFIX + category.getId();
        redisTemplate.opsForValue().set(key, category, Duration.ofHours(24));
        log.debug("Cached menu category: {}", key);
    }

    public void invalidateMenuCategory(Long id) {
        String key = MENU_CATEGORY_KEY_PREFIX + id;
        redisTemplate.delete(key);
        log.debug("Invalidated cache for menu category: {}", key);
    }

    public void clearAllMenuCache() {
        Set<String> menuKeys = redisTemplate.keys("menu:*");
        if (menuKeys != null && !menuKeys.isEmpty()) {
            redisTemplate.delete(menuKeys);
            log.info("Cleared all menu cache entries: {} keys", menuKeys.size());
        }
    }

    @Scheduled(cron = "0 0 3 * * *") // Run at 3 AM every day
    public void cleanupExpiredCache() {
        log.info("Starting scheduled cache cleanup");
        // Implementation of cleanup logic
        log.info("Completed scheduled cache cleanup");
    }
}