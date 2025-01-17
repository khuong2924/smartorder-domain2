package khuong.com.smartorderbeorderdomain.menu.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemPriceMessage {
    private Long menuItemId;
    private BigDecimal newPrice;
    private BigDecimal oldPrice;
    private LocalDateTime timestamp;
    private String reason;

    public MenuItemPriceMessage(Long menuItemId, BigDecimal newPrice) {
        this.menuItemId = menuItemId;
        this.newPrice = newPrice;
        this.timestamp = LocalDateTime.now();
    }
}
