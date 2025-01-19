package khuong.com.smartorderbeorderdomain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MenuItemPriceHistoryRepository extends JpaRepository<MenuItemPriceHistory, Long> {
    List<MenuItemPriceHistory> findByMenuItemIdOrderByChangedAtDesc(Long menuItemId);

    @Query("SELECT mph FROM MenuItemPriceHistory mph WHERE mph.menuItem.id = :menuItemId " +
            "AND mph.changedAt BETWEEN :startDate AND :endDate")
    List<MenuItemPriceHistory> findPriceHistoryBetweenDates(
            @Param("menuItemId") Long menuItemId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}