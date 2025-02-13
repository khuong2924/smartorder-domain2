package khuong.com.smartorder_domain2.order.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemRequest {
    private String orderId;
    private String menuItemId;
    private int quantity;
    private double unitPrice;
    private String status;
    private String specialNotes;
}