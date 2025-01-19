package khuong.com.smartorderbeorderdomain.menu.service;

import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemResponse;
import khuong.com.smartorderbeorderdomain.menu.entity.Category;
import khuong.com.smartorderbeorderdomain.menu.repository.CategoryRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuItemService {
    @Autowired
    private final MenuItemRepository menuItemRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    private final MenuMapper menuMapper;
    private final PriceHistoryService priceHistoryService;
    private final MenuItemOptionService optionService;

    @Transactional
    public MenuItemResponse createMenuItem(CreateMenuItemRequest request) {
        log.info("Creating new menu item: {}", request.getName());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        validateMenuItemName(request.getName(), request.getCategoryId());

        MenuItem menuItem = buildMenuItem(request, category);
        menuItem = menuItemRepository.save(menuItem);

        priceHistoryService.saveInitialPrice(menuItem);

        if (request.getOptions() != null) {
            optionService.createOptions(menuItem, request.getOptions());
        }

        return menuMapper.toMenuItemResponse(menuItem);
    }

    @Transactional
    public MenuItemResponse updateMenuItem(Long id, UpdateMenuItemRequest request) {
        MenuItem menuItem = getMenuItemEntity(id);

        if (request.getCategoryId() != null) {
            updateMenuItemCategory(menuItem, request.getCategoryId());
        }

        if (request.getName() != null && !request.getName().equals(menuItem.getName())) {
            validateMenuItemName(request.getName(), menuItem.getCategory().getId());
        }

        updateMenuItemFields(menuItem, request);

        if (request.getPrice() != null) {
            priceHistoryService.updatePrice(menuItem, request.getPrice(), "Price update");
        }

        if (request.getOptions() != null) {
            optionService.updateOptions(menuItem, request.getOptions());
        }

        return menuMapper.toMenuItemResponse(menuItemRepository.save(menuItem));
    }

    @Cacheable(value = "menuItems", key = "#id")
    public MenuItemResponse getMenuItemById(Long id) {
        return menuMapper.toMenuItemResponse(getMenuItemEntity(id));
    }

    public Page<MenuItemResponse> getAllMenuItems(Pageable pageable) {
        return menuItemRepository.findByActiveAndAvailable(true, true, pageable)
                .map(menuMapper::toMenuItemResponse);
    }

    // ... other methods and helpers
}
