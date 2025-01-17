package khuong.com.smartorderbeorderdomain.menu.mappers;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import org.springframework.web.bind.annotation.Mapping;

@Mapper(componentModel = "spring", uses = {MenuCategoryMapper.class})
public interface MenuItemMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    MenuItemDTO toDTO(MenuItem menuItem);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    MenuItem toEntity(MenuItemDTO dto);

    List<MenuItemDTO> toDTOList(List<MenuItem> menuItems);

    @Mapping(target = "menuItemId", source = "menuItem.id")
    @Mapping(target = "menuItemName", source = "menuItem.name")
    MenuItemPriceHistoryDTO toPriceHistoryDTO(MenuItemPriceHistory priceHistory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "menuItem", ignore = true)
    MenuItemOption toOptionEntity(MenuItemOptionDTO dto);

    @Mapping(target = "menuItemId", source = "menuItem.id")
    MenuItemOptionDTO toOptionDTO(MenuItemOption option);
}