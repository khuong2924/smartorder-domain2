package khuong.com.smartorderbeorderdomain.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MenuCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Integer displayOrder;
    private String imageUrl;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
