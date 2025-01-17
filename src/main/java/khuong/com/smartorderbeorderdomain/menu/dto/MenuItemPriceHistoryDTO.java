package khuong.com.smartorderbeorderdomain.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MenuItemPriceHistoryDTO {
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private LocalDateTime changedAt;
    private Long changedBy;
    private String reason;
}