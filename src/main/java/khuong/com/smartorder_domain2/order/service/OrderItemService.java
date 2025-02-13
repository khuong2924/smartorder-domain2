package khuong.com.smartorder_domain2.order.service;

import khuong.com.smartorder_domain2.menu.repository.MenuItemRepository;
import khuong.com.smartorder_domain2.order.dto.request.OrderItemRequest;
import khuong.com.smartorder_domain2.order.dto.response.OrderItemResponse;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.entity.OrderItem;
import khuong.com.smartorder_domain2.order.enums.OrderItemStatus;
import khuong.com.smartorder_domain2.order.repository.OrderItemRepository;
import khuong.com.smartorder_domain2.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    public OrderItemResponse getOrderItem(String id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));
        return OrderItemResponse.fromEntity(orderItem);
    }

    public List<OrderItemResponse> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        return orderItems.stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

//    public OrderItemResponse createOrderItem(OrderItemRequest request) {
//        Order order = orderRepository.findById(request.getOrderId())
//                .orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setOrder(order);
//        orderItem.setMenuItem(menuItemRepository.findById(Long.valueOf(request.getMenuItemId())).get());
//        orderItem.setQuantity(request.getQuantity());
//        orderItem.setUnitPrice(BigDecimal.valueOf(request.getUnitPrice()));
//        orderItem.setStatus(OrderItemStatus.valueOf(request.getStatus()));
//        orderItem.setSpecialNotes(request.getSpecialNotes());
//
//        orderItem = orderItemRepository.save(orderItem);
//        return OrderItemResponse.fromEntity(orderItem);
//    }

    public OrderItemResponse createOrderItem(OrderItemRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + request.getOrderId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItemRepository.findById(Long.valueOf(request.getMenuItemId())).get());
        orderItem.setQuantity(request.getQuantity());
        orderItem.setUnitPrice(BigDecimal.valueOf(request.getUnitPrice()));
        orderItem.setStatus(OrderItemStatus.valueOf(request.getStatus()));
        orderItem.setSpecialNotes(request.getSpecialNotes());

        orderItem = orderItemRepository.save(orderItem);
        return OrderItemResponse.fromEntity(orderItem);
    }

    public OrderItemResponse updateOrderItem(String id, OrderItemRequest request) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));

        orderItem.setMenuItem(menuItemRepository.findById(Long.valueOf(request.getMenuItemId())).get());
        orderItem.setQuantity(request.getQuantity());
        orderItem.setUnitPrice(BigDecimal.valueOf(request.getUnitPrice()));
        orderItem.setStatus(OrderItemStatus.valueOf(request.getStatus()));
        orderItem.setSpecialNotes(request.getSpecialNotes());

        orderItem = orderItemRepository.save(orderItem);
        return OrderItemResponse.fromEntity(orderItem);
    }

    public void deleteOrderItem(String id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));
        orderItemRepository.delete(orderItem);
    }
}