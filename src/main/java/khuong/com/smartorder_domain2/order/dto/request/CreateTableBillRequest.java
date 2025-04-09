package khuong.com.smartorder_domain2.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTableBillRequest {
    @NotBlank(message = "Table number is required")
    private String tableNumber;
    
    private String waiterId;
    
    private String note;
    
    @NotNull(message = "Payment method is required")
    private String paymentMethod;
}