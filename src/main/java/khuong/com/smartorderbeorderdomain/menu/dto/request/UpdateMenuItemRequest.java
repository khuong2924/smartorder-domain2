package khuong.com.smartorderbeorderdomain.menu.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class UpdateMenuItemRequest {
    private Long categoryId;

    @Size(max = 100)
    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private Integer preparationTime;
    private String imageUrl;
    private Boolean vegetarian;
    private Boolean spicy;
    private Integer calories;
    private Integer displayOrder;
    private Boolean available;
    private Set<String> allergens;
//    private List<UpdateMenuItemOptionRequest> options;
}
