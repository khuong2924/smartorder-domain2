package khuong.com.smartorder_domain2.service;

import com.pusher.rest.Pusher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PusherNotificationService {

    private final Pusher pusher;
    
    // config channel
    private static final String ORDER_UPDATES_CHANNEL = "order-updates";
    private static final String ORDER_ITEM_UPDATES_CHANNEL = "order-item-updates";
    private static final String MENU_UPDATES_CHANNEL = "menu-updates";
    
    // send notification about order status changes
     
    public void notifyOrderStatusUpdate(String orderId, String status) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("orderId", orderId);
            data.put("status", status);
            data.put("timestamp", System.currentTimeMillis());
            
            log.info("Sending Pusher notification for order status update: {}", data);
            pusher.trigger(ORDER_UPDATES_CHANNEL, "status-update", data);
        } catch (Exception e) {
            log.error("Failed to send Pusher notification for order status: {}", e.getMessage(), e);
        }
    }
    
    // send notification about order item status changes
    
    public void notifyOrderItemStatusUpdate(Long orderItemId, String status) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("orderItemId", orderItemId);
            data.put("status", status);
            data.put("timestamp", System.currentTimeMillis());
            
            log.info("Sending Pusher notification for order item status update: {}", data);
            pusher.trigger(ORDER_ITEM_UPDATES_CHANNEL, "item-status-update", data);
        } catch (Exception e) {
            log.error("Failed to send Pusher notification for order item status: {}", e.getMessage(), e);
        }
    }
    
    // send notification about menu item availability changes

    public void notifyMenuItemAvailabilityUpdate(Long menuItemId, boolean available, String menuItemName) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("menuItemId", menuItemId);
            data.put("name", menuItemName);
            data.put("available", available);
            data.put("timestamp", System.currentTimeMillis());
            
            log.info("Sending Pusher notification for menu item availability update: {}", data);
            pusher.trigger(MENU_UPDATES_CHANNEL, "availability-update", data);
        } catch (Exception e) {
            log.error("Failed to send Pusher notification for menu availability: {}", e.getMessage(), e);
        }
    }
}