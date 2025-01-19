package khuong.com.smartorderbeorderdomain.menu.service;

import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import khuong.com.smartorderbeorderdomain.configs.CacheConstants;
import khuong.com.smartorderbeorderdomain.menu.dto.exception.DuplicateResourceException;
import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.response.MenuItemResponse;
import khuong.com.smartorderbeorderdomain.menu.entity.Category;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;

import khuong.com.smartorderbeorderdomain.menu.repository.CategoryRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.common.errors.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final PriceHistoryService priceHistoryService;


    @Transactional
    public MenuItemResponse updateMenuItem(Long id, UpdateMenuItemRequest request) {
        MenuItem menuItem = findMenuItemById(id);

        if (request.getCategoryId() != null && !request.getCategoryId().equals(menuItem.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            menuItem.setCategory(newCategory);
        }

        if (request.getName() != null && !request.getName().equals(menuItem.getName())) {
            validateMenuItemName(request.getName(), menuItem.getCategory().getId());
            menuItem.setName(request.getName());
        }

        updateMenuItemFields(menuItem, request);

        if (request.getPrice() != null && !request.getPrice().equals(menuItem.getPrice())) {
            priceHistoryService.updatePrice(menuItem, request.getPrice(), "Price update");
        }

        menuItem = menuItemRepository.save(menuItem);
        return MenuItemResponse.fromEntity(menuItem);
    }

    @Cacheable(value = "menuItems", key = "#id")
    public MenuItemResponse getMenuItemById(Long id) {
        return MenuItemResponse.fromEntity(findMenuItemById(id));
    }

    public Page<MenuItemResponse> getAllMenuItems(Pageable pageable) {
        return menuItemRepository.findByActiveAndAvailable(true, true, pageable)
                .map(MenuItemResponse::fromEntity);
    }

    public List<MenuItemResponse> searchMenuItems(String keyword) {
        return menuItemRepository.searchMenuItems(keyword).stream()
                .map(MenuItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMenuItemAvailability(Long id, boolean available) {
        MenuItem menuItem = findMenuItemById(id);
        menuItem.setAvailable(available);
        menuItemRepository.save(menuItem);
    }

    MenuItem findMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    private void validateMenuItemName(String name, Long categoryId) {
        if (menuItemRepository.existsByNameIgnoreCaseAndCategoryId(name, categoryId)) {
            throw new DuplicateResourceException("Menu item already exists with name: " + name);
        }
    }

    private void updateMenuItemFields(MenuItem menuItem, UpdateMenuItemRequest request) {
        if (request.getDescription() != null) menuItem.setDescription(request.getDescription());
        if (request.getPreparationTime() != null) menuItem.setPreparationTime(request.getPreparationTime());
        if (request.getImageUrl() != null) menuItem.setImageUrl(request.getImageUrl());
        if (request.getVegetarian() != null) menuItem.setVegetarian(request.getVegetarian());
        if (request.getSpicy() != null) menuItem.setSpicy(request.getSpicy());
        if (request.getCalories() != null) menuItem.setCalories(request.getCalories());
        if (request.getAllergens() != null) menuItem.setAllergens(request.getAllergens());
    }


    @CacheEvict(cacheNames = CacheConstants.MENU_ITEM_CACHE, allEntries = true)
    @Transactional
    public MenuItemResponse createMenuItem(CreateMenuItemRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        validateMenuItemName(request.getName(), request.getCategoryId());

        MenuItem menuItem = MenuItem.builder()
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .preparationTime(request.getPreparationTime())
                .imageUrl(request.getImageUrl())
                .vegetarian(request.isVegetarian())
                .spicy(request.isSpicy())
                .calories(request.getCalories())
                .allergens(request.getAllergens())
                .available(true)
                .active(true)
                .build();

        menuItem = menuItemRepository.save(menuItem);
        priceHistoryService.saveInitialPrice(menuItem);

        return MenuItemResponse.fromEntity(menuItem);
    }
}
