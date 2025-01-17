package khuong.com.smartorderbeorderdomain.menu.payload.request;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class CreateMenuItemRequest {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private Integer preparationTime;
    private String imageUrl;
    private boolean vegetarian;
    private boolean spicy;
    private Integer calories;
    private Set<String> allergens;
    private List<CreateMenuItemOptionRequest> options;
    private Integer displayOrder;
    private ServingPeriod servingPeriod;
}