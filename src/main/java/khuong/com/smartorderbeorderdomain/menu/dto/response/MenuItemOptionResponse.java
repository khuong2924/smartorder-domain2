package khuong.com.smartorderbeorderdomain.menu.dto.response;


import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import khuong.com.smartorderbeorderdomain.menu.enums.OptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemOptionResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal additionalPrice;
    private String formattedAdditionalPrice;
    private boolean defaultOption;
    private boolean available;
    private OptionType optionType;
    private Integer minSelections;
    private Integer maxSelections;
    private List<OptionChoiceResponse> choices;

    public static MenuItemOptionResponse fromEntity(MenuItemOption option) {
        if (option == null) return null;

        return MenuItemOptionResponse.builder()
                .id(option.getId())
                .name(option.getName())
                .description(option.getDescription())
                .additionalPrice(option.getAdditionalPrice())
                .formattedAdditionalPrice(formatPrice(option.getAdditionalPrice()))
                .defaultOption(option.isDefaultOption())
                .available(option.isAvailable())
                .optionType(option.getOptionType())
                .minSelections(option.getMinSelections())
                .maxSelections(option.getMaxSelections())
                .choices(option.getChoices() != null ?
                        option.getChoices().stream()
                                .map(OptionChoiceResponse::fromEntity)
                                .collect(Collectors.toList()) :
                        null)
                .build();
    }

    private static String formatPrice(BigDecimal price) {
        if (price == null) return null;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }
}