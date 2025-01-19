package khuong.com.smartorderbeorderdomain.menu.mapper;

import khuong.com.smartorderbeorderdomain.menu.dto.response.CategoryResponse;
import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateCategoryRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateCategoryRequest;
import khuong.com.smartorderbeorderdomain.menu.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MenuItemMapper.class})
public interface CategoryMapper {

    @Mapping(target = "menuItems", ignore = true)
    Category toEntity(CreateCategoryRequest request);

    @Mapping(target = "menuItems", ignore = true)
    void updateEntityFromRequest(UpdateCategoryRequest request, @MappingTarget Category category);

    @Mapping(target = "menuItems", qualifiedByName = "toMenuItemBriefResponse")
    CategoryResponse toResponse(Category category);

    @Named("toCategoryBriefResponse")
    @Mapping(target = "menuItems", ignore = true)
    CategoryResponse toBriefResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    @AfterMapping
    default void afterToResponse(@MappingTarget CategoryResponse response) {
        // Additional processing if needed
    }
}
