package khuong.com.smartorderbeorderdomain.menu.repository;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.enums.ServingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryIdAndActiveTrue(Long categoryId);
    List<MenuItem> findByAvailableTrueAndActiveTrue();
    List<MenuItem> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    List<MenuItem> findByServingPeriodAndActiveTrue(ServingPeriod servingPeriod);
    List<MenuItem> findByVegetarianTrueAndActiveTrue();

    @Query("SELECT mi FROM MenuItem mi WHERE mi.active = true AND mi.available = true " +
            "AND mi.category.id = :categoryId ORDER BY mi.displayOrder")
    List<MenuItem> findAvailableItemsByCategory(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT mi FROM MenuItem mi JOIN mi.allergens a " +
            "WHERE mi.active = true AND :allergen NOT MEMBER OF mi.allergens")
    List<MenuItem> findByAllergenNotPresent(@Param("allergen") String allergen);
}