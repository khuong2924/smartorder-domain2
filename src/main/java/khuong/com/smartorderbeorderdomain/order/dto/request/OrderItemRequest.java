package khuong.com.smartorderbeorderdomain.order.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemRequest {
    private String menuItemId;
    private Integer quantity;
    private String specialNotes;
}