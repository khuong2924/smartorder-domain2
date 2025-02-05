package khuong.com.smartorderbeorderdomain.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem {
    @Id
    private String id;

    @ManyToOne
    private Cart cart;

    private String menuItemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String specialNotes;
}