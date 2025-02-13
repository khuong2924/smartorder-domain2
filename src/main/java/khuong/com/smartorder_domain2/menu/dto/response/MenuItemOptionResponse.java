package khuong.com.smartorder_domain2.menu.dto.response;

import khuong.com.smartorder_domain2.menu.entity.MenuItemOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemOptionResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public static MenuItemOptionResponse fromEntity(MenuItemOption option) {
        return MenuItemOptionResponse.builder()
                .id(option.getId())
                .name(option.getName())
                .price(option.getPrice())
                .build();
    }
}