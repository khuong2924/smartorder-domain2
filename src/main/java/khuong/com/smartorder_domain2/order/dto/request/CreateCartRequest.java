package khuong.com.smartorder_domain2.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartRequest {
    @NotNull(message = "Table ID is required")
    private Long tableId;
    private String userId;
}