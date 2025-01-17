package khuong.com.smartorderbeorderdomain.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuServiceClient menuServiceClient;
    private final BillingServiceClient billingServiceClient;

    public OrderDTO createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setTableNumber(request.getTableNumber());
        order.setWaiterId(request.getWaiterId());
        order.setCustomerId(request.getCustomerId());

        return mapToDTO(orderRepository.save(order));
    }

    public OrderDTO addItems(Long orderId, List<OrderItemRequest> items) {
        Order order = findOrder(orderId);

        items.forEach(itemRequest -> {
            // Validate with Menu Service
            MenuItemDTO menuItem = menuServiceClient.getMenuItem(itemRequest.getMenuItemId());
            if (!menuItem.isAvailable()) {
                throw new BadRequestException("Menu item not available: " + menuItem.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItemId(itemRequest.getMenuItemId());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setNotes(itemRequest.getNotes());
            orderItem.setUnitPrice(menuItem.getPrice());

            order.getItems().add(orderItem);
        });

        // Update billing information
        billingServiceClient.calculateBill(orderId);

        return mapToDTO(orderRepository.save(order));
    }
}