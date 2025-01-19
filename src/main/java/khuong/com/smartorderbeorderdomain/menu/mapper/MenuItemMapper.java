package khuong.com.smartorderbeorderdomain.menu.mapper;

import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemResponse;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {MenuItemOptionMapper.class})
public interface MenuItemMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "options", ignore = true)
    MenuItem toEntity(CreateMenuItemRequest request);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "options", ignore = true)
    void updateEntityFromRequest(UpdateMenuItemRequest request, @MappingTarget MenuItem menuItem);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    MenuItemResponse toResponse(MenuItem menuItem);

    @Named("toMenuItemBriefResponse")
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    MenuItemResponse toBriefResponse(MenuItem menuItem);

    List<MenuItemResponse> toResponseList(List<MenuItem> menuItems);

    default Page<MenuItemResponse> toResponsePage(Page<MenuItem> menuItems) {
        return menuItems.map(this::toResponse);
    }

    @AfterMapping
    default void afterToResponse(@MappingTarget MenuItemResponse response, MenuItem menuItem) {
        // Calculate additional fields if needed
        response.setFormattedPrice(formatPrice(menuItem.getPrice()));
    }

    default String formatPrice(BigDecimal price) {
        if (price == null) return null;
        return String.format("%,.2f VND", price);
    }
}
