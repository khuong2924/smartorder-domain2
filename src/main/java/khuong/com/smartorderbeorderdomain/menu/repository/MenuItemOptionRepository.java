package khuong.com.smartorderbeorderdomain.menu.repository;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemOptionRepository extends JpaRepository<MenuItemOption, Long> {

    // Lấy danh sách option theo món ăn
    List<MenuItemOption> findByMenuItemId(Long menuItemId);

    // Lấy danh sách option đang available
    List<MenuItemOption> findByMenuItemIdAndAvailableTrue(Long menuItemId);

    // Đếm số lượng option của món ăn
    long countByMenuItemId(Long menuItemId);

    // Cập nhật trạng thái available
    @Modifying
    @Query("UPDATE MenuItemOption o SET o.available = :available WHERE o.id = :id")
    void updateAvailability(@Param("id") Long id, @Param("available") boolean available);

    // Xóa tất cả option của món ăn
    @Modifying
    void deleteByMenuItemId(Long menuItemId);
}