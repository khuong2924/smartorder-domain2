package khuong.com.smartorderbeorderdomain.menu.dto.response;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class OptionChoiceResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal additionalPrice;
    private String formattedAdditionalPrice;
}