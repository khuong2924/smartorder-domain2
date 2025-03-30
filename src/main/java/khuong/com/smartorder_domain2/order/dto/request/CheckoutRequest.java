package khuong.com.smartorder_domain2.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private Long tableId;
    private String waiterId;
    private String note;
}