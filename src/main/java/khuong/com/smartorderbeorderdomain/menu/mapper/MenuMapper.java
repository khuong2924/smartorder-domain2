package khuong.com.smartorderbeorderdomain.menu.mapper;



import khuong.com.smartorderbeorderdomain.menu.dto.response.CategoryResponse;
import khuong.com.smartorderbeorderdomain.menu.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    @Mapping(target = "id", source = "category.id")
    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "description", source = "category.description")
    @Mapping(target = "displayOrder", source = "category.displayOrder")
    @Mapping(target = "active", source = "category.active")
    CategoryResponse toCategoryResponse(Category category);
}