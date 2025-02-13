package khuong.com.smartorder_domain2.menu.dto.response;

import khuong.com.smartorder_domain2.menu.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Integer displayOrder;
    private boolean active;
    private List<MenuItemResponse> menuItems;

    public static CategoryResponse fromEntity(Category category) {
        if (category == null) return null;

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .displayOrder(category.getDisplayOrder())
                .active(category.isActive())
                .menuItems(category.getMenuItems() != null ?
                        category.getMenuItems().stream()
                                .map(MenuItemResponse::fromEntity)
                                .collect(Collectors.toList()) :
                        null)
                .build();
    }
}