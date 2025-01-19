package khuong.com.smartorderbeorderdomain.menu.repository;

import khuong.com.smartorderbeorderdomain.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tìm danh mục theo tên
    Optional<Category> findByNameIgnoreCase(String name);

    // Lấy danh sách danh mục đang active
    List<Category> findByActiveTrue();

    // Lấy danh sách theo thứ tự hiển thị
    List<Category> findByActiveTrueOrderByDisplayOrderAsc();

    // Kiểm tra tên danh mục đã tồn tại chưa
    boolean existsByNameIgnoreCase(String name);

    // Tìm danh mục theo nhiều ID
    List<Category> findByIdIn(List<Long> ids);

    // Đếm số lượng món ăn trong danh mục
    @Query("SELECT COUNT(m) FROM MenuItem m WHERE m.category.id = :categoryId")
    long countMenuItemsByCategoryId(Long categoryId);
}