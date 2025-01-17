package khuong.com.smartorderbeorderdomain.menu.repository;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    Optional<MenuCategory> findByName(String name);
    List<MenuCategory> findByActiveTrue();
    List<MenuCategory> findByActiveTrueOrderByDisplayOrder();
    boolean existsByName(String name);
}