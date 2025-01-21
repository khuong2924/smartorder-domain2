package khuong.com.smartorderbeorderdomain.menu.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuItemRequest {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Menu item name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Preparation time cannot be negative")
    private Integer preparationTime;


    private MultipartFile image;

    private boolean vegetarian;
    private boolean spicy;

    @Min(value = 0, message = "Calories cannot be negative")
    private Integer calories;

    @Size(max = 10, message = "Cannot have more than 10 allergens")
    private Set<@NotBlank(message = "Allergen cannot be blank") String> allergens;

    @Valid
    private List<CreateMenuItemOptionRequest> options;
}