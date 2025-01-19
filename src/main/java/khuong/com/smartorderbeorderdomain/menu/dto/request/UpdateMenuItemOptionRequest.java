package khuong.com.smartorderbeorderdomain.menu.dto.request;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class UpdateMenuItemOptionRequest {
    private String name;
    private String description;
    private BigDecimal additionalPrice;
    private Boolean defaultOption;
    private String optionType;
    private Integer minSelections;
    private Integer maxSelections;
    private Boolean available;
}