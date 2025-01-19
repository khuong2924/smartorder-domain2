package khuong.com.smartorderbeorderdomain.menu.dto.response;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private BigDecimal price;
    private String formattedPrice;
    private Integer preparationTime;
    private String imageUrl;
    private boolean vegetarian;
    private boolean spicy;
    private Integer calories;
    private boolean available;
    private Set<String> allergens;
    private List<MenuItemOptionResponse> options;

    public static MenuItemResponse fromEntity(MenuItem menuItem) {
        if (menuItem == null) return null;

        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .categoryId(menuItem.getCategory().getId())
                .categoryName(menuItem.getCategory().getName())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .formattedPrice(formatPrice(menuItem.getPrice()))
                .preparationTime(menuItem.getPreparationTime())
                .imageUrl(menuItem.getImageUrl())
                .vegetarian(menuItem.isVegetarian())
                .spicy(menuItem.isSpicy())
                .calories(menuItem.getCalories())
                .available(menuItem.isAvailable())
                .allergens(menuItem.getAllergens())
                .options(menuItem.getOptions() != null ?
                        menuItem.getOptions().stream()
                                .map(MenuItemOptionResponse::fromEntity)
                                .collect(Collectors.toList()) :
                        null)
                .build();
    }

    private static String formatPrice(BigDecimal price) {
        if (price == null) return null;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }
}