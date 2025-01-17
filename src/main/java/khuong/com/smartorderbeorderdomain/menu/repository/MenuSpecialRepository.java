package khuong.com.smartorderbeorderdomain.menu.repository;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuSpecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MenuSpecialRepository extends JpaRepository<MenuSpecial, Long> {
    List<MenuSpecial> findByActiveTrue();

    @Query("SELECT ms FROM MenuSpecial ms WHERE ms.active = true " +
            "AND :currentDate BETWEEN ms.startDate AND ms.endDate")
    List<MenuSpecial> findActiveSpecials(@Param("currentDate") LocalDateTime currentDate);

    List<MenuSpecial> findByActiveTrueAndEndDateAfter(LocalDateTime date);
}
