package khuong.com.smartorderbeorderdomain.order.dto.request;

import khuong.com.smartorderbeorderdomain.order.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
