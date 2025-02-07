package khuong.com.smartorderbeorderdomain.order.service;


import khuong.com.smartorderbeorderdomain.order.entity.Order;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    public void notifyKitchen(Order order) {
        //

        System.out.println("Notifying kitchen about order: " + order.getId());
    }

    public void notifyOrderStatusChange(Order order) {
        //

        System.out.println("Notifying about order status change: " + order.getId() + " to " + order.getStatus());
    }
}
