package khuong.com.smartorderbeorderdomain.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "menu_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "preparation_time")
    private Integer preparationTime;

    @Column(name = "image_url")
    private String imageUrl;

    private boolean vegetarian;
    private boolean spicy;
    private Integer calories;


    @Column(name = "display_order")
    private Integer displayOrder;

    private boolean available = true;
    private boolean active = true;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MenuItemOption> options;

//    @ElementCollection
    @CollectionTable(
            name = "menu_item_allergens",
            joinColumns = @JoinColumn(name = "menu_item_id")
    )
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> allergens;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}