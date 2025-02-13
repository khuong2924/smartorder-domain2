package khuong.com.smartorder_domain2.order.repository;

import khuong.com.smartorder_domain2.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}