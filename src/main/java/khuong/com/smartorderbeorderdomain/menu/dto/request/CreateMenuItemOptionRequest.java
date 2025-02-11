package khuong.com.smartorderbeorderdomain.menu.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import khuong.com.smartorderbeorderdomain.menu.enums.OptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuItemOptionRequest {
    @NotBlank(message = "Option name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.0", message = "Additional price cannot be negative")
    private BigDecimal additionalPrice;

//    private boolean defaultOption;
//
//    @NotNull(message = "Option type is required")
//    private OptionType optionType;

    @Min(value = 0, message = "Minimum selections cannot be negative")
    private Integer minSelections;

    @Min(value = 1, message = "Maximum selections must be at least 1")
    private Integer maxSelections;

    @Valid
    @Size(min = 1, message = "At least one choice is required")
    private List<CreateOptionChoiceRequest> choices;
}