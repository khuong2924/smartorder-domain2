package khuong.com.smartorderbeorderdomain.menu.mappers;

import khuong.com.smartorderbeorderdomain.menu.dto.MenuCategoryDTO;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuCategory;
import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuCategoryMapper {
    @Mapping(target = "menuItems", ignore = true)
    MenuCategoryDTO toDTO(MenuCategory category);

    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    MenuCategory toEntity(MenuCategoryDTO dto);

    List<MenuCategoryDTO> toDTOList(List<MenuCategory> categories);
}
