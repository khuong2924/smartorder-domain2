//package khuong.com.smartorderbeorderdomain.order.service;
//
//import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
//import khuong.com.smartorderbeorderdomain.menu.service.MenuItemService;
//import khuong.com.smartorderbeorderdomain.order.dto.exception.InvalidOrderStatusException;
//import khuong.com.smartorderbeorderdomain.order.dto.exception.OrderNotFoundException;
//import khuong.com.smartorderbeorderdomain.order.enums.OrderItemStatus;
//import khuong.com.smartorderbeorderdomain.order.enums.OrderStatus;
//import khuong.com.smartorderbeorderdomain.order.repository.OrderItemRepository;
//import khuong.com.smartorderbeorderdomain.order.repository.OrderRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import khuong.com.smartorderbeorderdomain.order.dto.request.CreateOrderRequest;
//import khuong.com.smartorderbeorderdomain.order.dto.request.OrderItemRequest;
//import khuong.com.smartorderbeorderdomain.order.dto.response.OrderResponse;
//import khuong.com.smartorderbeorderdomain.order.entity.Order;
//import khuong.com.smartorderbeorderdomain.order.entity.OrderItem;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class OrderService {
//    private final OrderRepository orderRepository;
//    private final OrderItemRepository orderItemRepository;
//    private final MenuItemService menuItemService;
//    private final WebSocketService webSocketService;
//
//    public OrderResponse createOrder(CreateOrderRequest request) {
//        Order order = new Order();
//        order.setId(UUID.randomUUID().toString());
//        order.setTableNumber(request.getTableNumber());
//        order.setWaiterId(request.getWaiterId());
//        order.setStatus(OrderStatus.PENDING);
//        order.setNote(request.getNote());
//
//        List<OrderItem> items = request.getItems().stream()
//                .map(item -> createOrderItem(item, order))
//                .collect(Collectors.toList());
//
//        order.setItems(items);
//        order.setTotalAmount(calculateTotalAmount(items));
//
//        Order savedOrder = orderRepository.save(order);
//        webSocketService.notifyKitchen(savedOrder);
//
//        return OrderMapper.toResponse(savedOrder);
//    }
//
//    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new OrderNotFoundException(orderId));
//
//        validateStatusTransition(order.getStatus(), newStatus);
//        order.setStatus(newStatus);
//
//        Order updatedOrder = orderRepository.save(order);
//        webSocketService.notifyOrderStatusChange(updatedOrder);
//
//        return OrderMapper.toResponse(updatedOrder);
//    }
//
//    private OrderItem createOrderItem(OrderItemRequest request, Order order) {
//        MenuItem menuItem = menuItemService.getMenuItem(request.getMenuItemId());
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setId(UUID.randomUUID().toString());
//        orderItem.setOrder(order);
//        orderItem.setMenuItem(menuItem);
//        orderItem.setQuantity(request.getQuantity());
//        orderItem.setUnitPrice(menuItem.getPrice());
//        orderItem.setStatus(OrderItemStatus.PENDING);
//        orderItem.setSpecialNotes(request.getSpecialNotes());
//
//        return orderItem;
//    }
//
//    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
//        return items.stream()
//                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
//        // Add validation logic for status transitions
//        if (!isValidStatusTransition(currentStatus, newStatus)) {
//            throw new InvalidOrderStatusException(
//                    "Invalid status transition from " + currentStatus + " to " + newStatus
//            );
//        }
//    }
//}