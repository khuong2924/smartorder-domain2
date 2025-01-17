package khuong.com.smartorderbeorderdomain.menu.payload.response;

import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MenuUpdateMessage {
    private String updateType; // NEW_ITEM, ITEM_UPDATED, ITEM_DELETED
    private MenuItemDTO menuItem;
    private LocalDateTime timestamp;
}
