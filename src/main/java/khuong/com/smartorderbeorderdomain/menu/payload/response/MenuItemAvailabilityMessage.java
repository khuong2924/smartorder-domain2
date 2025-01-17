package khuong.com.smartorderbeorderdomain.menu.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemAvailabilityMessage {
    private Long menuItemId;
    private boolean available;
    private LocalDateTime timestamp;

    public MenuItemAvailabilityMessage(Long menuItemId, boolean available) {
        this.menuItemId = menuItemId;
        this.available = available;
        this.timestamp = LocalDateTime.now();
    }
}