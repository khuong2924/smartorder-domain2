package khuong.com.smartorder_domain2.order.repository;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByTableNumber(String tableNumber);
    List<Order> findByWaiterId(String waiterId);
    List<Order> findByStatus(OrderStatus status);
}
