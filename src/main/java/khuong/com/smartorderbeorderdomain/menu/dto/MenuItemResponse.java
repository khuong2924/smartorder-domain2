package khuong.com.smartorderbeorderdomain.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class MenuItemResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer preparationTime;
    private String imageUrl;
    private boolean vegetarian;
    private boolean spicy;
    private Integer calories;
    private Integer displayOrder;
    private boolean available;
    private boolean active;
    private Set<String> allergens;
    private List<MenuItemOptionResponse> options;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
