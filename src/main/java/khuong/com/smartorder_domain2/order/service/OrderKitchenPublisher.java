package khuong.com.smartorder_domain2.order.service;

import khuong.com.smartorder_domain2.order.dto.OrderToKitchenDto;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component("orderServiceKitchenPublisher")
@Slf4j
@RequiredArgsConstructor
public class OrderKitchenPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${kitchen.exchange.orders}")
    private String ordersExchange;

    @Value("${kitchen.routing-key.orders}")
    private String ordersRoutingKey;

    public void publishOrderToKitchen(Order order, List<OrderItem> newItems) {
        OrderToKitchenDto dto = createKitchenDto(order, newItems);
        
        rabbitTemplate.convertAndSend(ordersExchange, ordersRoutingKey, dto);
        
        log.info("Published order {} to kitchen with {} items", order.getId(), newItems.size());
    }

    private OrderToKitchenDto createKitchenDto(Order order, List<OrderItem> items) {
        List<OrderToKitchenDto.KitchenItemDto> kitchenItems = items.stream()
                .map(item -> OrderToKitchenDto.KitchenItemDto.builder()
                        .itemId(item.getMenuItem().getId())
                        .itemName(item.getMenuItem().getName())
                        .quantity(item.getQuantity())
                        .notes(item.getSpecialNotes())
                        .build())
                .collect(Collectors.toList());

        return OrderToKitchenDto.builder()
                .orderId(order.getId())
                .tableNumber(order.getTable().getTableNumber())
                .items(kitchenItems)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}