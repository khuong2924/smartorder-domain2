package khuong.com.smartorder_domain2.order.entity;

import jakarta.persistence.*;
import khuong.com.smartorder_domain2.order.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "table_id")
    private khuong.com.smartorder_domain2.table.entity.Table table;
    
    @OneToMany(mappedBy = "bill")
    private List<Order> orders = new ArrayList<>();
    
    private BigDecimal totalAmount;
    
    private String paymentMethod;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    private LocalDateTime createdAt;
    
    private String note;
    
    private String waiterId;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}