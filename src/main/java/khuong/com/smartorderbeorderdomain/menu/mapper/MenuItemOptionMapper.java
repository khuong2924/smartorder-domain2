package khuong.com.smartorderbeorderdomain.menu.mapper;

import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateMenuItemOptionRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateMenuItemOptionRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemOptionResponse;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {OptionChoiceMapper.class})
public interface MenuItemOptionMapper {

    @Mapping(target = "menuItem", ignore = true)
    @Mapping(target = "choices", ignore = true)
    MenuItemOption toEntity(CreateMenuItemOptionRequest request);

    @Mapping(target = "menuItem", ignore = true)
    @Mapping(target = "choices", ignore = true)
    void updateEntityFromRequest(UpdateMenuItemOptionRequest request, @MappingTarget MenuItemOption option);

    MenuItemOptionResponse toResponse(MenuItemOption option);

    @Named("toOptionBriefResponse")
    @Mapping(target = "choices", ignore = true)
    MenuItemOptionResponse toBriefResponse(MenuItemOption option);

    List<MenuItemOptionResponse> toResponseList(List<MenuItemOption> options);

    @AfterMapping
    default void afterToResponse(@MappingTarget MenuItemOptionResponse response, MenuItemOption option) {
        if (option.getAdditionalPrice() != null) {
            response.setFormattedAdditionalPrice(formatPrice(option.getAdditionalPrice()));
        }
    }

    default String formatPrice(BigDecimal price) {
        if (price == null) return null;
        return String.format("%,.2f VND", price);
    }
}