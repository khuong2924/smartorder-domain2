package khuong.com.smartorderbeorderdomain.order.repository;

import khuong.com.smartorderbeorderdomain.order.entity.Order;
import khuong.com.smartorderbeorderdomain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByWaiterId(Long waiterId);
    List<Order> findByCustomerId(Long customerId);
    Optional<Order> findByTableNumberAndStatus(Integer tableNumber, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate")
    List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}