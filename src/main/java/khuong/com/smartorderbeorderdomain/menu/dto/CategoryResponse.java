package khuong.com.smartorderbeorderdomain.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Integer displayOrder;
    private boolean active;
    private List<MenuItemResponse> menuItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}