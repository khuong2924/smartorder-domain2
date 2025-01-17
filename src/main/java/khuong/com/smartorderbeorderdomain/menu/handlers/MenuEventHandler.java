package khuong.com.smartorderbeorderdomain.menu.handlers;

import khuong.com.smartorderbeorderdomain.menu.payload.response.MenuItemMessage;
import khuong.com.smartorderbeorderdomain.menu.service.MenuCacheService;
import khuong.com.smartorderbeorderdomain.menu.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MenuEventHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MenuCacheService menuCacheService;
    private final WebSocketService webSocketService;

    @EventListener
    public void handleMenuItemCreated(MenuItemCreatedEvent event) {
        log.info("Handling menu item created event: {}", event.getMenuItem().getId());

        // Cache the new menu item
        menuCacheService.cacheMenuItem(event.getMenuItem());

        // Notify other services
        kafkaTemplate.send("menu-items", new MenuItemMessage(
                "ITEM_CREATED",
                event.getMenuItem()
        ));

        // Notify connected clients
        webSocketService.notifyMenuUpdate("NEW_ITEM", event.getMenuItem());
    }

    @EventListener
    public void handleMenuItemUpdated(MenuItemUpdatedEvent event) {
        log.info("Handling menu item updated event: {}", event.getMenuItem().getId());

        // Invalidate and update cache
        menuCacheService.invalidateMenuItem(event.getMenuItem().getId());
        menuCacheService.cacheMenuItem(event.getMenuItem());

        // Notify other services
        kafkaTemplate.send("menu-items", new MenuItemMessage(
                "ITEM_UPDATED",
                event.getMenuItem()
        ));

        // Notify connected clients
        webSocketService.notifyMenuUpdate("ITEM_UPDATED", event.getMenuItem());
    }

    @EventListener
    public void handleMenuItemAvailabilityChanged(MenuItemAvailabilityChangedEvent event) {
        log.info("Handling menu item availability change: {}", event.getMenuItemId());

        // Notify other services
        kafkaTemplate.send("menu-items", new MenuItemAvailabilityMessage(
                event.getMenuItemId(),
                event.isAvailable()
        ));

        // Notify connected clients
        webSocketService.notifyAvailabilityChange(event.getMenuItemId(), event.isAvailable());
    }

    @EventListener
    public void handleMenuItemPriceChanged(MenuItemPriceChangedEvent event) {
        log.info("Handling menu item price change: {}", event.getMenuItemId());

        // Notify other services
        kafkaTemplate.send("menu-items", new MenuItemPriceMessage(
                event.getMenuItemId(),
                event.getNewPrice()
        ));

        // Notify connected clients
        webSocketService.notifyPriceChange(event.getMenuItemId(), event.getNewPrice());
    }
}