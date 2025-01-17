package khuong.com.smartorderbeorderdomain.order.dto;

import khuong.com.smartorderbeorderdomain.order.enums.OrderItemStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDTO {
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String notes;
    private OrderItemStatus status;
    private BigDecimal subtotal;
}