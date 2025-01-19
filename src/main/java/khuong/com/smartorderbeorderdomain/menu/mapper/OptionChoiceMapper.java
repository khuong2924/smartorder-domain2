package khuong.com.smartorderbeorderdomain.menu.mapper;

import khuong.com.smartorderbeorderdomain.menu.entity.OptionChoice;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionChoiceMapper {

    @Mapping(target = "option", ignore = true)
    OptionChoice toEntity(CreateOptionChoiceRequest request);

    @Mapping(target = "option", ignore = true)
    void updateEntityFromRequest(UpdateOptionChoiceRequest request, @MappingTarget OptionChoice choice);

    OptionChoiceResponse toResponse(OptionChoice choice);

    List<OptionChoiceResponse> toResponseList(List<OptionChoice> choices);

    @AfterMapping
    default void afterToResponse(@MappingTarget OptionChoiceResponse response, OptionChoice choice) {
        if (choice.getAdditionalPrice() != null) {
            response.setFormattedAdditionalPrice(formatPrice(choice.getAdditionalPrice()));
        }
    }

    default String formatPrice(BigDecimal price) {
        if (price == null) return null;
        return String.format("%,.2f VND", price);
    }
}