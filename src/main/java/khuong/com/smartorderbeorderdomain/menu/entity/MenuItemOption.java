package khuong.com.smartorderbeorderdomain.menu.entity;

import jakarta.persistence.*;
import khuong.com.smartorderbeorderdomain.menu.enums.OptionType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menu_item_options")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemOption implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false, length = 100)
    private String name;

    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "additional_price", precision = 10, scale = 2)
    private BigDecimal additionalPrice;

    @Column(name = "default_option")
    private boolean defaultOption;

    private boolean available = true;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_type", length = 20)
    private OptionType optionType = OptionType.SINGLE;

    @Column(name = "min_selections")
    private Integer minSelections = 0;

    @Column(name = "max_selections")
    private Integer maxSelections = 1;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    private List<OptionChoice> choices;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}