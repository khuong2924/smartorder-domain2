package khuong.com.smartorderbeorderdomain.menu.handlers;

import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuItemUpdatedEvent {
    private final MenuItemDTO menuItem;
    private final String updateType;
}