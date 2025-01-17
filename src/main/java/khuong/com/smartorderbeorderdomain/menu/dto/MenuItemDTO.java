package khuong.com.smartorderbeorderdomain.menu.dto;

import khuong.com.smartorderbeorderdomain.menu.enums.ServingPeriod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class MenuItemDTO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer preparationTime;
    private String imageUrl;
    private boolean available;
    private boolean vegetarian;
    private boolean spicy;
    private Integer calories;
    private Set<String> allergens;
    private List<MenuItemOptionDTO> options;
    private Integer displayOrder;
    private ServingPeriod servingPeriod;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}