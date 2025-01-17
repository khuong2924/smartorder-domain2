package khuong.com.smartorderbeorderdomain.menu.service;

import khuong.com.smartorderbeorderdomain.menu.dto.MenuCategoryDTO;
import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String MENU_ITEM_CACHE_KEY = "menu:item:";
    private static final String MENU_CATEGORY_CACHE_KEY = "menu:category:";
    private static final Duration CACHE_DURATION = Duration.ofHours(24);

    public void cacheMenuItem(MenuItemDTO menuItem) {
        String key = MENU_ITEM_CACHE_KEY + menuItem.getId();
        redisTemplate.opsForValue().set(key, menuItem, CACHE_DURATION);
    }

    public Optional<MenuItemDTO> getCachedMenuItem(Long id) {
        String key = MENU_ITEM_CACHE_KEY + id;
        return Optional.ofNullable((MenuItemDTO) redisTemplate.opsForValue().get(key));
    }

    public void invalidateMenuItem(Long id) {
        String key = MENU_ITEM_CACHE_KEY + id;
        redisTemplate.delete(key);
    }

    public void cacheMenuCategory(MenuCategoryDTO category) {
        String key = MENU_CATEGORY_CACHE_KEY + category.getId();
        redisTemplate.opsForValue().set(key, category, CACHE_DURATION);
    }

    public void invalidateMenuCategory(Long id) {
        String key = MENU_CATEGORY_CACHE_KEY + id;
        redisTemplate.delete(key);
    }
}