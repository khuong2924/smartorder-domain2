package khuong.com.smartorderbeorderdomain.menu.payload.response;

import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemPriceHistoryDTO;
import khuong.com.smartorderbeorderdomain.menu.dto.MenuSpecialDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MenuItemDetailResponse {
    private MenuItemDTO item;
    private List<MenuItemPriceHistoryDTO> priceHistory;
    private List<MenuSpecialDTO> activeSpecials;
}