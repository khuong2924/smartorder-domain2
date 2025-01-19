package khuong.com.smartorderbeorderdomain.menu.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCategoryRequest {
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    private String name;

    private String description;
    private Integer displayOrder;
    private Boolean active;
}
