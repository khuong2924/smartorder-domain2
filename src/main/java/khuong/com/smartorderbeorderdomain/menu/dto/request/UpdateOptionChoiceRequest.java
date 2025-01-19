package khuong.com.smartorderbeorderdomain.menu.dto.request;



import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOptionChoiceRequest {
    private Long id;

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.0", message = "Additional price cannot be negative")
    private BigDecimal additionalPrice;

    @Min(value = 0, message = "Display order cannot be negative")
    private Integer displayOrder;

    private Boolean available;
}