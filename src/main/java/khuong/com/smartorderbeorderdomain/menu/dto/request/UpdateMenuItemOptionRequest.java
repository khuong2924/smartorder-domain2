package khuong.com.smartorderbeorderdomain.menu.dto.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateMenuItemOptionRequest {

    @NotNull(message = "Option ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private BigDecimal additionalPrice;

    private List<UpdateOptionChoiceRequest> choices;
}