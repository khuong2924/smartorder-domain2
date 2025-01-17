package khuong.com.smartorderbeorderdomain.menu.repository;

import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemOptionRepository extends JpaRepository<MenuItemOption, Long> {
    List<MenuItemOption> findByMenuItemId(Long menuItemId);
    List<MenuItemOption> findByMenuItemIdAndAvailableTrue(Long menuItemId);
    List<MenuItemOption> findByMenuItemIdOrderByDisplayOrder(Long menuItemId);
    void deleteByMenuItemId(Long menuItemId);
}
