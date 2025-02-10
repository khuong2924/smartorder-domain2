// src/main/java/khuong/com/smartorderbeorderdomain/order/service/OrderService.java
package khuong.com.smartorderbeorderdomain.order.service;

import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemResponse;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.service.MenuItemService;
import khuong.com.smartorderbeorderdomain.order.dto.exception.InvalidOrderStatusException;
import khuong.com.smartorderbeorderdomain.order.dto.exception.OrderNotFoundException;
import khuong.com.smartorderbeorderdomain.order.enums.OrderItemStatus;
import khuong.com.smartorderbeorderdomain.order.enums.OrderStatus;
import khuong.com.smartorderbeorderdomain.order.repository.OrderItemRepository;
import khuong.com.smartorderbeorderdomain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import khuong.com.smartorderbeorderdomain.order.dto.request.CreateOrderRequest;
import khuong.com.smartorderbeorderdomain.order.dto.request.OrderItemRequest;
import khuong.com.smartorderbeorderdomain.order.dto.response.OrderResponse;
import khuong.com.smartorderbeorderdomain.order.entity.Order;
import khuong.com.smartorderbeorderdomain.order.entity.OrderItem;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final OrderItemRepository orderItemRepository;
    private final MenuItemService menuItemService;
    private final WebSocketService webSocketService;
    @Autowired
    private  RestTemplate restTemplate;


    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setTableNumber(request.getTableNumber());
        String waiterId = getLoggedInUserId();
        if (waiterId == null) {
            throw new IllegalArgumentException("User is not logged in");
        }
        order.setStatus(OrderStatus.PENDING);
        order.setNote(request.getNote());

        List<OrderItem> items = request.getItems().stream()
                .map(item -> createOrderItem(item, order))
                .collect(Collectors.toList());

        order.setItems(items);
        order.setTotalAmount(calculateTotalAmount(items));

        Order savedOrder = orderRepository.save(order);
        webSocketService.notifyKitchen(savedOrder);

        return OrderResponse.fromEntity(savedOrder);
    }

    private String getLoggedInUserId() {
        String url = "http://localhost:8080/users/me";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            // Assuming the response body contains the user ID
            return response.getBody();
        }
        return null;
    }

    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);
        webSocketService.notifyOrderStatusChange(updatedOrder);

        return OrderResponse.fromEntity(updatedOrder);
    }

    private OrderItem createOrderItem(OrderItemRequest request, Order order) {
        MenuItemResponse menuItem = menuItemService.getMenuItemById(Long.valueOf(request.getMenuItemId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID().toString());
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem.toEntity(menuItem));
        orderItem.setQuantity(request.getQuantity());
        orderItem.setUnitPrice(menuItem.getPrice());
        orderItem.setStatus(OrderItemStatus.PENDING);
        orderItem.setSpecialNotes(request.getSpecialNotes());

        return orderItem;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new InvalidOrderStatusException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus
            );
        }
    }

    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Add validation logic for status transitions
        if (currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.CONFIRMED) {
            return true;
        }
        if (currentStatus == OrderStatus.CONFIRMED && newStatus == OrderStatus.COMPLETED) {
            return true;
        }
        return false;
    }
}