package khuong.com.smartorder_domain2.order.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import khuong.com.smartorder_domain2.order.entity.OrderItem;
import khuong.com.smartorder_domain2.order.enums.OrderItemStatus;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByStatus(OrderItemStatus status);
}
