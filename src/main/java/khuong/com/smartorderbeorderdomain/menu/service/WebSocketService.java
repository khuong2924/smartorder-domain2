package khuong.com.smartorderbeorderdomain.menu.service;


import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import khuong.com.smartorderbeorderdomain.menu.payload.response.MenuUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public void notifyMenuUpdate(String updateType, MenuItemDTO menuItem) {
        MenuUpdateMessage message = new MenuUpdateMessage(
                updateType,
                menuItem,
                LocalDateTime.now()
        );

        String destination = String.format("/topic/menu/items/%d", menuItem.getId());
        messagingTemplate.convertAndSend(destination, message);
        log.debug("Sent menu update notification: {}", message);
    }

    public void notifyAvailabilityChange(Long menuItemId, boolean available) {
        AvailabilityChangeMessage message = new AvailabilityChangeMessage(
                menuItemId,
                available,
                LocalDateTime.now()
        );

        String destination = String.format("/topic/menu/items/%d/availability", menuItemId);
        messagingTemplate.convertAndSend(destination, message);
        log.debug("Sent availability change notification: {}", message);
    }

    public void notifyPriceChange(Long menuItemId, BigDecimal newPrice) {
        PriceChangeMessage message = new PriceChangeMessage(
                menuItemId,
                newPrice,
                LocalDateTime.now()
        );

        String destination = String.format("/topic/menu/items/%d/price", menuItemId);
        messagingTemplate.convertAndSend(destination, message);
        log.debug("Sent price change notification: {}", message);
    }

    public void notifyCategoryUpdate(String updateType, MenuCategoryDTO category) {
        CategoryUpdateMessage message = new CategoryUpdateMessage(
                updateType,
                category,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/menu/categories", message);
        log.debug("Sent category update notification: {}", message);
    }
}