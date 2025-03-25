package khuong.com.smartorder_domain2.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Table ID is required")
    private Long tableId;
    private String waiterId;
    private List<OrderItemRequest> items;
    private String note;
}

