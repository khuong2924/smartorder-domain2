package khuong.com.smartorderbeorderdomain.order.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import khuong.com.smartorderbeorderdomain.order.entity.OrderItem;
import khuong.com.smartorderbeorderdomain.order.enums.OrderItemStatus;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    List<OrderItem> findByOrderId(String orderId);
    List<OrderItem> findByStatus(OrderItemStatus status);
}
