package khuong.com.smartorderbeorderdomain.order.dto.response;

import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemResponse;
import khuong.com.smartorderbeorderdomain.order.enums.OrderItemStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemResponse {
    private String id;
    private MenuItemResponse menuItem;
    private Integer quantity;
    private BigDecimal unitPrice;
    private OrderItemStatus status;
    private String specialNotes;
}