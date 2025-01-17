package khuong.com.smartorderbeorderdomain.order.service;

import jakarta.transaction.Transactional;
import khuong.com.smartorderbeorderdomain.order.dto.OrderDTO;
import khuong.com.smartorderbeorderdomain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuServiceClient menuServiceClient;
    private final BillingServiceClient billingServiceClient;
    private final WebSocketService webSocketService;

    public OrderDTO createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setTableNumber(request.getTableNumber());
        order.setCreatedBy(request.getCreatedBy());

        Order savedOrder = orderRepository.save(order);
        webSocketService.notifyNewOrder(mapToDTO(savedOrder));
        return mapToDTO(savedOrder);
    }

    public OrderDTO addItems(Long orderId, List<AddOrderItemRequest> items) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        List<OrderItem> newItems = items.stream()
                .map(item -> {
                    MenuItemDTO menuItem = menuServiceClient.getMenuItem(item.getMenuItemId());
                    if (!menuItem.isAvailable()) {
                        throw new BadRequestException("Menu item not available: " + menuItem.getName());
                    }

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItemId(menuItem.getId());
                    orderItem.setMenuItemName(menuItem.getName());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setUnitPrice(menuItem.getPrice());
                    orderItem.setNotes(item.getNotes());
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.getItems().addAll(newItems);
        updateOrderAmount(order);

        Order savedOrder = orderRepository.save(order);
        webSocketService.notifyOrderUpdated(mapToDTO(savedOrder));
        return mapToDTO(savedOrder);
    }

    private void updateOrderAmount(Order order) {
        BillingDetailsDTO billingDetails = billingServiceClient.calculateBill(
                order.getId(),
                order.getItems().stream()
                        .map(this::mapToOrderItemDTO)
                        .collect(Collectors.toList())
        );

        order.setTotalAmount(billingDetails.getTotalAmount());
        order.setTax(billingDetails.getTax());
        order.setDiscount(billingDetails.getDiscount());
        order.setFinalAmount(billingDetails.getFinalAmount());
    }
}