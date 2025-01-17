package khuong.com.smartorderbeorderdomain.menu.payload.response;

import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemMessage {
    private String eventType;
    private MenuItemDTO menuItem;
    private LocalDateTime timestamp;

    public MenuItemMessage(String eventType, MenuItemDTO menuItem) {
        this.eventType = eventType;
        this.menuItem = menuItem;
        this.timestamp = LocalDateTime.now();
    }
}