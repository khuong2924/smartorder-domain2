package khuong.com.smartorderbeorderdomain.order.dto.response;
import khuong.com.smartorderbeorderdomain.order.enums.OrderStatus;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String tableNumber;
    private String waiterId;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

