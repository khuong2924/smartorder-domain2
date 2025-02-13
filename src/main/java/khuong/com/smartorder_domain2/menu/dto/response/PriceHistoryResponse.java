package khuong.com.smartorder_domain2.menu.dto.response;

import khuong.com.smartorder_domain2.menu.entity.PriceHistory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PriceHistoryResponse {
    private Long id;
    private Long menuItemId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String reason;
    private String changedBy;
    private LocalDateTime changedAt;

    public static PriceHistoryResponse fromEntity(PriceHistory priceHistory) {
        return PriceHistoryResponse.builder()
                .id(priceHistory.getId())
                .menuItemId(priceHistory.getMenuItem().getId())
                .oldPrice(priceHistory.getOldPrice())
                .newPrice(priceHistory.getNewPrice())
                .reason(priceHistory.getReason())
                .changedBy(priceHistory.getChangedBy())
                .changedAt(priceHistory.getChangedAt())
                .build();
    }
}
