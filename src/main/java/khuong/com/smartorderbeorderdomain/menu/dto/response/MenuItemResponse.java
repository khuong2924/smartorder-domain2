package khuong.com.smartorderbeorderdomain.menu.dto.response;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer preparationTime;
    private String imageUrl;
    private String categoryName;
    private List<MenuItemOptionResponse> options;

    public static MenuItemResponse fromEntity(MenuItem menuItem) {
        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .preparationTime(menuItem.getPreparationTime())
                .imageUrl(menuItem.getImageUrl())

                .categoryName(menuItem.getCategory().getName())
                .options(menuItem.getOptions().stream()
                        .map(MenuItemOptionResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public MenuItem toEntity(MenuItemResponse response) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(response.getId());
        menuItem.setName(response.getName());
        menuItem.setDescription(response.getDescription());
        menuItem.setPrice(response.getPrice());
        menuItem.setPreparationTime(response.getPreparationTime());
        menuItem.setImageUrl(response.getImageUrl());

        return menuItem;
    }
}