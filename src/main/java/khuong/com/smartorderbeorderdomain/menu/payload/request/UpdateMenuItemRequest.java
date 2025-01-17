package khuong.com.smartorderbeorderdomain.menu.payload.request;

import lombok.Data;

@Data
public class UpdateMenuItemRequest {
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private Integer preparationTime;
    private String imageUrl;
    private Boolean available;
    private Boolean vegetarian;
    private Boolean spicy;
    private Integer calories;
    private Set<String> allergens;
    private List<UpdateMenuItemOptionRequest> options;
    private Integer displayOrder;
    private ServingPeriod servingPeriod;
    private String priceChangeReason;
}
