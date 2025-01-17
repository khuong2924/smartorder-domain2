package khuong.com.smartorderbeorderdomain.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MenuItemOptionDTO {
    private Long id;
    private Long menuItemId;
    private String name;
    private String description;
    private BigDecimal additionalPrice;
    private boolean defaultOption;
    private boolean available;
    private Integer displayOrder;
}