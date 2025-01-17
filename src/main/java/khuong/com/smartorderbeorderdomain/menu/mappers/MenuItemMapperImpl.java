package khuong.com.smartorderbeorderdomain.menu.mappers;

import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class MenuItemMapperImpl implements MenuItemMapper {
    private final MenuCategoryMapper categoryMapper;

    public MenuItemMapperImpl(MenuCategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public MenuItemDTO toDTO(MenuItem menuItem) {
        if (menuItem == null) {
            return null;
        }

        return MenuItemDTO.builder()
                .id(menuItem.getId())
                .categoryId(menuItem.getCategory().getId())
                .categoryName(menuItem.getCategory().getName())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .preparationTime(menuItem.getPreparationTime())
                .imageUrl(menuItem.getImageUrl())
                .available(menuItem.isAvailable())
                .vegetarian(menuItem.isVegetarian())
                .spicy(menuItem.isSpicy())
                .calories(menuItem.getCalories())
                .allergens(new HashSet<>(menuItem.getAllergens()))
                .options(menuItem.getOptions().stream()
                        .map(this::toOptionDTO)
                        .collect(Collectors.toList()))
                .displayOrder(menuItem.getDisplayOrder())
                .servingPeriod(menuItem.getServingPeriod())
                .active(menuItem.isActive())
                .createdAt(menuItem.getCreatedAt())
                .updatedAt(menuItem.getUpdatedAt())
                .build();
    }

    @Override
    public MenuItem toEntity(MenuItemDTO dto) {
        if (dto == null) {
            return null;
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setId(dto.getId());
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setPreparationTime(dto.getPreparationTime());
        menuItem.setImageUrl(dto.getImageUrl());
        menuItem.setAvailable(dto.isAvailable());
        menuItem.setVegetarian(dto.isVegetarian());
        menuItem.setSpicy(dto.isSpicy());
        menuItem.setCalories(dto.getCalories());
        menuItem.setAllergens(new HashSet<>(dto.getAllergens()));
        menuItem.setDisplayOrder(dto.getDisplayOrder());
        menuItem.setServingPeriod(dto.getServingPeriod());
        menuItem.setActive(dto.isActive());

        return menuItem;
    }
}