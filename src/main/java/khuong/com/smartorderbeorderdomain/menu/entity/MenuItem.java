package khuong.com.smartorderbeorderdomain.menu.entity;

import jakarta.persistence.*;
import khuong.com.smartorderbeorderdomain.menu.enums.ServingPeriod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menu_items")
@Getter @Setter
@NoArgsConstructor
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MenuCategory category;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "preparation_time")
    private Integer preparationTime; // in minutes

    @Column(name = "image_url")
    private String imageUrl;

    private boolean available = true;

    @Column(name = "is_vegetarian")
    private boolean vegetarian;

    @Column(name = "is_spicy")
    private boolean spicy;

    @Column(name = "calories")
    private Integer calories;

    @ElementCollection
    @CollectionTable(
            name = "menu_item_allergens",
            joinColumns = @JoinColumn(name = "menu_item_id")
    )
    @Column(name = "allergen")
    private Set<String> allergens = new HashSet<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItemOption> options = new ArrayList<>();

    @Column(name = "display_order")
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "serving_period")
    private ServingPeriod servingPeriod;

    private boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}