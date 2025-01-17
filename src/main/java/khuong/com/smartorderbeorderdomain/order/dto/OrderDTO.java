package khuong.com.smartorderbeorderdomain.order.dto;

import khuong.com.smartorderbeorderdomain.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private Integer tableNumber;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private Long waiterId;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal totalAmount;
}
