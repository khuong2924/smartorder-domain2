package khuong.com.smartorder_domain2.order.service;

import khuong.com.smartorder_domain2.menu.dto.exception.ResourceNotFoundException;
import khuong.com.smartorder_domain2.menu.dto.response.MenuItemResponse;
import khuong.com.smartorder_domain2.menu.service.MenuItemService;
import khuong.com.smartorder_domain2.order.dto.exception.InvalidOrderStatusException;
import khuong.com.smartorder_domain2.order.dto.exception.OrderNotFoundException;
import khuong.com.smartorder_domain2.order.enums.OrderItemStatus;
import khuong.com.smartorder_domain2.order.enums.OrderStatus;
import khuong.com.smartorder_domain2.order.repository.OrderItemRepository;
import khuong.com.smartorder_domain2.order.repository.OrderRepository;
import khuong.com.smartorder_domain2.security.TokenExtractor;
import khuong.com.smartorder_domain2.table.entity.Table;
import khuong.com.smartorder_domain2.table.enums.TableStatus;
import khuong.com.smartorder_domain2.table.repository.TableRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import khuong.com.smartorder_domain2.order.dto.request.CreateOrderRequest;
import khuong.com.smartorder_domain2.order.dto.request.OrderItemRequest;
import khuong.com.smartorder_domain2.order.dto.response.OrderResponse;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.entity.OrderItem;
import khuong.com.smartorder_domain2.order.service.OrderKitchenPublisher;

import java.math.BigDecimal;
import java.util.List;

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

    @Autowired
    private final TableRepository tableRepository;

    @Autowired
    private TokenExtractor tokenExtractor;
    @Autowired
    @Qualifier("orderServiceKitchenPublisher")
    private OrderKitchenPublisher orderKitchenPublisher;

    public OrderResponse createOrder(CreateOrderRequest request, String authHeader) {
        Order order = new Order();
        
        // Find table by id and update its status
        Table table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + request.getTableId()));
        
        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new IllegalStateException("Table is not available");
        }
        
        // Update table status to OCCUPIED
        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);
        
        order.setTable(table);
        
        // Lấy ID người dùng từ token
        String waiterId = tokenExtractor.extractUserId(authHeader);
        if (waiterId == null) {
            throw new IllegalStateException("Người dùng chưa xác thực");
        }
        
        order.setWaiterId(waiterId);
        order.setStatus(OrderStatus.PENDING);
        order.setNote(request.getNote());

        List<OrderItem> items = request.getItems().stream()
                .map(item -> createOrderItem(item, order))
                .collect(Collectors.toList());

        order.setItems(items);
        order.setTotalAmount(calculateTotalAmount(items));

        Order savedOrder = orderRepository.save(order);
        
        // Publish to kitchen
        orderKitchenPublisher.publishOrderToKitchen(savedOrder, items);

        return OrderResponse.fromEntity(savedOrder);
    }

    // private String getLoggedInUserId() {
    //     String url = "http://localhost:8080/users/me";
    //     ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    //     if (response.getStatusCode().is2xxSuccessful()) {
    //         // Assuming the response body contains the user ID
    //         return response.getBody();
    //     }
    //     return null;
    // }

    public OrderResponse updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);
        // webSocketService.notifyOrderStatusChange(updatedOrder);

        return OrderResponse.fromEntity(updatedOrder);
    }

    private OrderItem createOrderItem(OrderItemRequest request, Order order) {
        MenuItemResponse menuItem = menuItemService.getMenuItemById(Long.valueOf(request.getMenuItemId()));

        OrderItem orderItem = new OrderItem();
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
        // Định nghĩa các chuyển đổi trạng thái hợp lệ
        switch (currentStatus) {
            case PENDING:
                if (newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Không thể chuyển từ PENDING sang " + newStatus);
                }
                break;
            case CONFIRMED:
                if (newStatus != OrderStatus.IN_PROGRESS && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Không thể chuyển từ CONFIRMED sang " + newStatus);
                }
                break;
            case IN_PROGRESS:
                if (newStatus != OrderStatus.READY && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Không thể chuyển từ IN_PROGRESS sang " + newStatus);
                }
                break;
            case READY:
                if (newStatus != OrderStatus.DELIVERED && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Không thể chuyển từ READY sang " + newStatus);
                }
                break;
            case DELIVERED:
                if (newStatus != OrderStatus.COMPLETED && newStatus != OrderStatus.CANCELLED) {
                    throw new IllegalStateException("Không thể chuyển từ DELIVERED sang " + newStatus);
                }
                break;
            case COMPLETED:
                throw new IllegalStateException("Không thể thay đổi trạng thái của đơn hàng đã hoàn thành");
            case CANCELLED:
                throw new IllegalStateException("Không thể thay đổi trạng thái của đơn hàng đã hủy");
        }
    }

    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return OrderResponse.fromEntity(order);
    }
    public List<OrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    // Add method to close table when order is completed
    public void closeTable(Long orderId) {
        Order order = orderRepository.findById(String.valueOf(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
        
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            Table table = order.getTable();
            table.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(table);
        } else {
            throw new InvalidOrderStatusException("Order must be completed or cancelled to close the table");
        }
    }

    @Transactional
    public void updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepository.findById(String.valueOf(orderItemId))
                .orElseThrow(() -> new OrderNotFoundException("Order item not found: " + orderItemId));
        
        OrderItemStatus newStatus = OrderItemStatus.valueOf(status);
        orderItem.setStatus(newStatus);
        orderItemRepository.save(orderItem);
        
        // Cập nhật trạng thái đơn hàng nếu cần
        Order order = orderItem.getOrder();
        updateOrderStatusIfNeeded(order);
    }

    private void updateOrderStatusIfNeeded(Order order) {
        boolean allCompleted = order.getItems().stream()
                .allMatch(item -> item.getStatus() == OrderItemStatus.COMPLETED);
        
        if (allCompleted) {
            // Quy tắc chuyển đổi trạng thái khi tất cả món ăn hoàn thành
            switch(order.getStatus()) {
                case PENDING:
                case CONFIRMED:
                    // Nếu đang ở trạng thái PENDING hoặc CONFIRMED, chuyển sang READY
                    order.setStatus(OrderStatus.READY);
                    orderRepository.save(order);
                    break;
                case IN_PROGRESS:
                    // Nếu đang ở trạng thái IN_PROGRESS, chuyển sang READY
                    order.setStatus(OrderStatus.READY);
                    orderRepository.save(order);
                    break;
                // Các trạng thái khác giữ nguyên
            }
        }
    }
}