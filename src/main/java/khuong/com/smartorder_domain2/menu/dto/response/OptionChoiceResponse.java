package khuong.com.smartorder_domain2.menu.dto.response;


import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;


import khuong.com.smartorder_domain2.menu.entity.OptionChoice;

import java.text.NumberFormat;
import java.util.Locale;

@Data
@Builder
public class OptionChoiceResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal additionalPrice;
    private String formattedAdditionalPrice;

    public static OptionChoiceResponse fromEntity(OptionChoice optionChoice) {
        if (optionChoice == null) return null;

        return OptionChoiceResponse.builder()
                .id(optionChoice.getId())
                .name(optionChoice.getName())
                .description(optionChoice.getDescription())
                .additionalPrice(optionChoice.getAdditionalPrice())
                .formattedAdditionalPrice(formatPrice(optionChoice.getAdditionalPrice()))
                .build();
    }

    private static String formatPrice(BigDecimal price) {
        if (price == null) return null;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }
}