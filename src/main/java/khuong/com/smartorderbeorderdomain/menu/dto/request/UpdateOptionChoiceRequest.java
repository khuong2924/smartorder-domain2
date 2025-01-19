package khuong.com.smartorderbeorderdomain.menu.dto.request;



import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class UpdateOptionChoiceRequest {

    @NotNull(message = "Choice ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private BigDecimal additionalPrice;
}