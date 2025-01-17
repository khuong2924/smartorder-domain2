package khuong.com.smartorderbeorderdomain.order.payload.request;

import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@Builder
public class CreateOrderRequest {
    @NotNull(message = "Table number is required")
    private Integer tableNumber;

    @NotNull(message = "Waiter ID is required")
    private Long waiterId;

    private Long customerId;
}