package khuong.com.smartorder_domain2.messaging;

import khuong.com.smartorder_domain2.menu.service.MenuItemService;
import khuong.com.smartorder_domain2.order.enums.OrderStatus;
import khuong.com.smartorder_domain2.order.service.OrderService;
import khuong.com.smartorder_domain2.service.PusherNotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class KitchenUpdateListener {
    private final OrderService orderService;
    private final MenuItemService menuItemService;
    private final PusherNotificationService pusherNotificationService;

    @RabbitListener(queues = "${kitchen.queue.order-updates}")
    public void handleOrderItemStatusUpdate(Map<String, Object> message) {
        log.info("Received order item status update: {}", message);
        try {
            Long orderItemId = Long.valueOf(message.get("orderItemId").toString());
            String status = message.get("status").toString();
            orderService.updateOrderItemStatus(orderItemId, status);
            
            //  real-time notification 
            pusherNotificationService.notifyOrderItemStatusUpdate(orderItemId, status);
            
            log.info("Successfully updated order item {} status to {}", orderItemId, status);
        } catch (Exception e) {
            log.error("Error processing order item status update: {}", e.getMessage(), e);
            throw e;
        }
    }

    @RabbitListener(queues = "${kitchen.queue.order-updates}")
    public void handleOrderStatusUpdate(Map<String, Object> message) {
        log.info("Received order status update: {}", message);
        try {
            String orderId = message.get("orderId").toString();
            String status = message.get("status").toString();
            
            // Update order status
            orderService.updateOrderStatus(orderId, OrderStatus.valueOf(status));
            
            // Send real-time notification 
            pusherNotificationService.notifyOrderStatusUpdate(orderId, status);
            
            log.info("Successfully updated order {} status to {}", orderId, status);
        } catch (Exception e) {
            log.error("Error processing order status update: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = "${kitchen.queue.menu-updates}")
    public void handleMenuItemAvailabilityUpdate(Map<String, Object> message) {
        log.info("Received menu item availability update: {}", message);
        try {
            Long menuItemId = Long.valueOf(message.get("menuItemId").toString());
            boolean available = (boolean) message.get("available");
            String menuItemName = message.get("name") != null ? message.get("name").toString() : "Unknown Item";
            
            menuItemService.updateMenuItemAvailability(menuItemId, available);
            
            // real-time notification 
            pusherNotificationService.notifyMenuItemAvailabilityUpdate(menuItemId, available, menuItemName);
            
            log.info("Successfully updated menu item {} availability to {}", menuItemId, available);
        } catch (Exception e) {
            log.error("Error processing menu item availability update: {}", e.getMessage(), e);
            throw e;
        }
    }
}