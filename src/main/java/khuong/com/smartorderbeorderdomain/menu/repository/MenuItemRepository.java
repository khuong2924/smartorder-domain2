package khuong.com.smartorderbeorderdomain.menu.repository;
import org.springframework.data.domain.Pageable;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    Page<MenuItem> findByActiveAndAvailable(boolean active, boolean available, Pageable pageable);

    // Tìm món ăn theo category
    List<MenuItem> findByCategoryId(Long categoryId);

    // Tìm kiếm món ăn theo tên hoặc mô tả
    @Query("SELECT m FROM MenuItem m WHERE " +
            "(LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND m.active = true")
    List<MenuItem> searchMenuItems(@Param("keyword") String keyword);



    // Tìm món ăn theo khoảng giá
    List<MenuItem> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    // Kiểm tra tên món ăn đã tồn tại chưa
    boolean existsByNameIgnoreCaseAndCategoryId(String name, Long categoryId);

    // Cập nhật trạng thái available
    @Modifying
    @Query("UPDATE MenuItem m SET m.available = :available WHERE m.id = :id")
    void updateAvailability(@Param("id") Long id, @Param("available") boolean available);

    // Cập nhật giá món ăn
    @Modifying
    @Query("UPDATE MenuItem m SET m.price = :newPrice WHERE m.id = :id")
    void updatePrice(@Param("id") Long id, @Param("newPrice") BigDecimal newPrice);
}