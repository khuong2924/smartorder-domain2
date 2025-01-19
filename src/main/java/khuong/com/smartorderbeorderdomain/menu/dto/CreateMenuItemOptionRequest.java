package khuong.com.smartorderbeorderdomain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import khuong.com.smartorderbeorderdomain.menu.enums.OptionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateMenuItemOptionRequest {
    @NotBlank(message = "Option name is required")
    @Size(max = 100)
    private String name;

    private String description;
    private BigDecimal additionalPrice;
    private boolean defaultOption;
    private Integer displayOrder;
    private OptionType optionType;
    private Integer minSelections;
    private Integer maxSelections;
//    private List<CreateOptionChoiceRequest> choices;
}
