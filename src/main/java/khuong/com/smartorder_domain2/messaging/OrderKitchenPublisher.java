package khuong.com.smartorder_domain2.messaging;

import khuong.com.smartorder_domain2.order.dto.OrderToKitchenDto;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderKitchenPublisher {
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${kitchen.exchange.orders}")
    private String ordersExchange;
    
    @Value("${kitchen.routing-key.orders}")
    private String ordersRoutingKey;
    
    public void publishOrderToKitchen(Order order, List<OrderItem> items) {
        try {
            OrderToKitchenDto orderDto = createOrderDto(order, items);
            
            log.info("Publishing order to kitchen: {}", orderDto);
            rabbitTemplate.convertAndSend(ordersExchange, ordersRoutingKey, orderDto);
            log.info("Order published to kitchen successfully");
        } catch (Exception e) {
            log.error("Error publishing order to kitchen: {}", e.getMessage(), e);
        }
    }
    
    private OrderToKitchenDto createOrderDto(Order order, List<OrderItem> items) {
        List<OrderToKitchenDto.KitchenItemDto> kitchenItems = items.stream()
                .map(item -> OrderToKitchenDto.KitchenItemDto.builder()
                        .itemId(item.getMenuItem().getId())
                        .itemName(item.getMenuItem().getName())
                        .quantity(item.getQuantity())
                        .notes(item.getSpecialNotes())
                        .build())
                .collect(Collectors.toList());
        
        return OrderToKitchenDto.builder()
                .orderId(Long.valueOf(order.getId()))
                .tableNumber(order.getTable().getTableNumber())
                .items(kitchenItems)
                .timestamp(Instant.now().toEpochMilli())
                .build();
    }
}