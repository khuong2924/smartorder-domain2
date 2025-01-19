package khuong.com.smartorderbeorderdomain.menu.dto.response;

import khuong.com.smartorderbeorderdomain.menu.enums.OptionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MenuItemOptionResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal additionalPrice;
    private boolean defaultOption;
    private boolean available;
    private Integer displayOrder;
    private OptionType optionType;
    private Integer minSelections;
    private Integer maxSelections;
//    private List<OptionChoiceResponse> choices;
}
