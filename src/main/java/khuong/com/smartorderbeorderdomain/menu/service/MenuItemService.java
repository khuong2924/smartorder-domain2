package khuong.com.smartorderbeorderdomain.menu.service;

import jakarta.transaction.Transactional;
import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuCategory;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemPriceHistory;
import khuong.com.smartorderbeorderdomain.menu.payload.request.CreateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.payload.response.MenuItemDetailResponse;
import khuong.com.smartorderbeorderdomain.menu.payload.response.MenuItemMessage;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuCategoryRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemPriceHistoryRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuItemService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String MENU_TOPIC = "menu-items";

    public void sendMenuItemCreatedMessage(MenuItemDTO menuItem) {
        MenuItemMessage message = new MenuItemMessage(
                MenuEventType.ITEM_CREATED,
                menuItem
        );

        kafkaTemplate.send(MENU_TOPIC, message)
                .addCallback(
                        result -> log.info("Menu item created message sent successfully"),
                        ex -> log.error("Failed to send menu item created message", ex)
                );
    }

    public void sendMenuItemUpdatedMessage(MenuItemDTO menuItem) {
        MenuItemMessage message = new MenuItemMessage(
                MenuEventType.ITEM_UPDATED,
                menuItem
        );

        kafkaTemplate.send(MENU_TOPIC, message)
                .addCallback(
                        result -> log.info("Menu item updated message sent successfully"),
                        ex -> log.error("Failed to send menu item updated message", ex)
                );
    }

    public void sendAvailabilityChangedMessage(Long menuItemId, boolean available) {
        MenuItemAvailabilityMessage message = new MenuItemAvailabilityMessage(
                menuItemId,
                available
        );

        kafkaTemplate.send(MENU_TOPIC, message)
                .addCallback(
                        result -> log.info("Availability changed message sent successfully"),
                        ex -> log.error("Failed to send availability changed message", ex)
                );
    }

    public void sendPriceChangedMessage(Long menuItemId, BigDecimal newPrice, BigDecimal oldPrice, String reason) {
        MenuItemPriceMessage message = new MenuItemPriceMessage(
                menuItemId,
                newPrice,
                oldPrice,
                LocalDateTime.now(),
                reason
        );

        kafkaTemplate.send(MENU_TOPIC, message)
                .addCallback(
                        result -> log.info("Price changed message sent successfully"),
                        ex -> log.error("Failed to send price changed message", ex)
                );
    }
}