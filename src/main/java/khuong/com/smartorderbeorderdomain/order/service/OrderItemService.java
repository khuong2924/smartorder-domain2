
package khuong.com.smartorderbeorderdomain.order.service;

import khuong.com.smartorderbeorderdomain.order.dto.response.OrderItemResponse;
import khuong.com.smartorderbeorderdomain.order.entity.OrderItem;
import khuong.com.smartorderbeorderdomain.order.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemResponse getOrderItem(String id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + id));
        return OrderItemResponse.fromEntity(orderItem);
    }


}