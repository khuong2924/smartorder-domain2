package khuong.com.smartorder_domain2.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderToKitchenDto {
    private Long orderId;
    private String tableNumber;
    private List<KitchenItemDto> items;
    private Long timestamp;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KitchenItemDto {
        private Long itemId;
        private String itemName;
        private int quantity;
        private String notes;
    }
}