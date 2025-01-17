package khuong.com.smartorderbeorderdomain.menu.listener;

import khuong.com.smartorderbeorderdomain.menu.payload.response.MenuItemAvailabilityMessage;
import khuong.com.smartorderbeorderdomain.menu.payload.response.MenuItemMessage;
import khuong.com.smartorderbeorderdomain.menu.payload.response.MenuItemPriceMessage;
import khuong.com.smartorderbeorderdomain.menu.service.MenuCacheService;
import khuong.com.smartorderbeorderdomain.menu.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MenuMessageListener {
    private final MenuCacheService cacheService;
    private final WebSocketService webSocketService;

    @KafkaListener(topics = "menu-items", groupId = "menu-service-group")
    public void handleMenuMessage(ConsumerRecord<String, Object> record) {
        Object value = record.value();

        try {
            if (value instanceof MenuItemMessage) {
                handleMenuItemMessage((MenuItemMessage) value);
            } else if (value instanceof MenuItemAvailabilityMessage) {
                handleAvailabilityMessage((MenuItemAvailabilityMessage) value);
            } else if (value instanceof MenuItemPriceMessage) {
                handlePriceMessage((MenuItemPriceMessage) value);
            } else {
                log.warn("Unknown message type received: {}", value.getClass());
            }
        } catch (Exception e) {
            log.error("Error processing menu message", e);
        }
    }

    private void handleMenuItemMessage(MenuItemMessage message) {
        switch (message.getEventType()) {
            case MenuEventType.ITEM_CREATED:
            case MenuEventType.ITEM_UPDATED:
                cacheService.cacheMenuItem(message.getMenuItem());
                webSocketService.notifyMenuUpdate(message.getEventType(), message.getMenuItem());
                break;
            case MenuEventType.ITEM_DELETED:
                cacheService.invalidateMenuItem(message.getMenuItem().getId());
                webSocketService.notifyMenuUpdate(message.getEventType(), message.getMenuItem());
                break;
            default:
                log.warn("Unknown event type: {}", message.getEventType());
        }
    }

    private void handleAvailabilityMessage(MenuItemAvailabilityMessage message) {
        webSocketService.notifyAvailabilityChange(message.getMenuItemId(), message.isAvailable());
    }

    private void handlePriceMessage(MenuItemPriceMessage message) {
        webSocketService.notifyPriceChange(message.getMenuItemId(), message.getNewPrice());
    }
}