package khuong.com.smartorder_domain2.order.repository;
import khuong.com.smartorder_domain2.order.entity.Order;
import khuong.com.smartorder_domain2.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT o FROM Order o WHERE o.table.tableNumber = :tableNumber")
    List<Order> findByTableNumber(@Param("tableNumber") String tableNumber);
    
    List<Order> findByWaiterId(String waiterId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByTableIdAndBillIsNullAndStatusNot(Long tableId, OrderStatus status);
}
