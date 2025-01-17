package khuong.com.smartorderbeorderdomain.menu.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class MenuSpecialDTO {
    private Long id;
    private String name;
    private String description;
    private Set<MenuItemDTO> items;
    private BigDecimal specialPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean active;
}