package khuong.com.smartorder_domain2.menu.repository;

import khuong.com.smartorder_domain2.menu.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    // Lấy lịch sử giá của món ăn
    List<PriceHistory> findByMenuItemIdOrderByChangedAtDesc(Long menuItemId);

    // Lấy lịch sử giá trong khoảng thời gian
    List<PriceHistory> findByMenuItemIdAndChangedAtBetween(
            Long menuItemId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Lấy giá gần nhất của món ăn
    Optional<PriceHistory> findFirstByMenuItemIdOrderByChangedAtDesc(Long menuItemId);
}