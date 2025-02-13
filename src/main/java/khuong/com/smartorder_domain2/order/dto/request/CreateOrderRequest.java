package khuong.com.smartorder_domain2.order.dto.request;

import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateOrderRequest {
    private String tableNumber;
    private String waiterId;
    private List<OrderItemRequest> items;
    private String note;
}

