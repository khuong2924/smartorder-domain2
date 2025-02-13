package khuong.com.smartorder_domain2.order.dto.request;

import khuong.com.smartorder_domain2.order.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
