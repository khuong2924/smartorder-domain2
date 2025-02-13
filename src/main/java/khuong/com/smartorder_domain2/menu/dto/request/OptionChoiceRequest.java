package khuong.com.smartorder_domain2.menu.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OptionChoiceRequest {
    private Long menuItemOptionId;
    private String name;
    private String description;
    private BigDecimal additionalPrice;
    private boolean defaultChoice;
    private boolean available;
    private Integer displayOrder;
}