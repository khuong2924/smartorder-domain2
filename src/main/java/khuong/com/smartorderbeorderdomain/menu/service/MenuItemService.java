package khuong.com.smartorderbeorderdomain.menu.service;

import jakarta.transaction.Transactional;
import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuCategory;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemOption;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuItemPriceHistory;
import khuong.com.smartorderbeorderdomain.menu.payload.request.CreateMenuItemRequest;
import khuong.com.smartorderbeorderdomain.menu.payload.response.MenuItemDetailResponse;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuCategoryRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemPriceHistoryRepository;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository categoryRepository;
    private final MenuItemPriceHistoryRepository priceHistoryRepository;
    private final MenuItemMapper menuItemMapper;
    private final ApplicationEventPublisher eventPublisher;

    public MenuItemDTO createMenuItem(CreateMenuItemRequest request) {
        MenuCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        MenuItem menuItem = new MenuItem();
        menuItem.setCategory(category);
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setPreparationTime(request.getPreparationTime());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setVegetarian(request.isVegetarian());
        menuItem.setSpicy(request.isSpicy());
        menuItem.setCalories(request.getCalories());
        menuItem.setAllergens(request.getAllergens());
        menuItem.setServingPeriod(request.getServingPeriod());
        menuItem.setDisplayOrder(request.getDisplayOrder());
        menuItem.setCreatedBy(SecurityUtils.getCurrentUserId());

        if (request.getOptions() != null) {
            List<MenuItemOption> options = request.getOptions().stream()
                    .map(optionRequest -> {
                        MenuItemOption option = new MenuItemOption();
                        option.setMenuItem(menuItem);
                        option.setName(optionRequest.getName());
                        option.setDescription(optionRequest.getDescription());
                        option.setAdditionalPrice(optionRequest.getAdditionalPrice());
                        option.setDefaultOption(optionRequest.isDefaultOption());
                        option.setDisplayOrder(optionRequest.getDisplayOrder());
                        return option;
                    })
                    .collect(Collectors.toList());
            menuItem.setOptions(options);
        }

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        log.info("Created new menu item: {}", savedMenuItem.getName());
        return menuItemMapper.toDTO(savedMenuItem);
    }

    public MenuItemDTO updateMenuItem(Long id, UpdateMenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (request.getPrice() != null && !request.getPrice().equals(menuItem.getPrice())) {
            createPriceHistory(menuItem, request.getPrice(), request.getPriceChangeReason());
            menuItem.setPrice(request.getPrice());
        }

        Optional.ofNullable(request.getName()).ifPresent(menuItem::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(menuItem::setDescription);
        Optional.ofNullable(request.getPreparationTime()).ifPresent(menuItem::setPreparationTime);
        Optional.ofNullable(request.getImageUrl()).ifPresent(menuItem::setImageUrl);
        Optional.ofNullable(request.getAvailable()).ifPresent(available -> {
            menuItem.setAvailable(available);
            eventPublisher.publishEvent(new MenuItemAvailabilityChangedEvent(menuItem.getId(), available));
        });
        Optional.ofNullable(request.getVegetarian()).ifPresent(menuItem::setVegetarian);
        Optional.ofNullable(request.getSpicy()).ifPresent(menuItem::setSpicy);
        Optional.ofNullable(request.getCalories()).ifPresent(menuItem::setCalories);
        Optional.ofNullable(request.getAllergens()).ifPresent(menuItem::setAllergens);
        Optional.ofNullable(request.getServingPeriod()).ifPresent(menuItem::setServingPeriod);
        Optional.ofNullable(request.getDisplayOrder()).ifPresent(menuItem::setDisplayOrder);

        menuItem.setUpdatedBy(SecurityUtils.getCurrentUserId());
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        log.info("Updated menu item: {}", updatedMenuItem.getName());
        return menuItemMapper.toDTO(updatedMenuItem);
    }

    private void createPriceHistory(MenuItem menuItem, BigDecimal newPrice, String reason) {
        MenuItemPriceHistory priceHistory = new MenuItemPriceHistory();
        priceHistory.setMenuItem(menuItem);
        priceHistory.setOldPrice(menuItem.getPrice());
        priceHistory.setNewPrice(newPrice);
        priceHistory.setChangedAt(LocalDateTime.now());
        priceHistory.setChangedBy(SecurityUtils.getCurrentUserId());
        priceHistory.setReason(reason);
        priceHistoryRepository.save(priceHistory);

        eventPublisher.publishEvent(new MenuItemPriceChangedEvent(menuItem.getId(), newPrice));
    }

    public MenuItemDetailResponse getMenuItemDetail(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        List<MenuItemPriceHistory> priceHistory = priceHistoryRepository
                .findByMenuItemIdOrderByChangedAtDesc(id);

        return MenuItemDetailResponse.builder()
                .item(menuItemMapper.toDTO(menuItem))
                .priceHistory(priceHistory.stream()
                        .map(menuItemMapper::toPriceHistoryDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public List<MenuItemDTO> getAvailableItemsByCategory(Long categoryId) {
        return menuItemRepository.findAvailableItemsByCategory(categoryId).stream()
                .map(menuItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> searchItems(MenuItemSearchCriteria criteria) {
        Specification<MenuItem> spec = Specification.where(null);

        if (criteria.getCategoryId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("id"), criteria.getCategoryId()));
        }

        if (criteria.isAvailableOnly()) {
            spec = spec.and((root, query, cb) ->
                    cb.isTrue(root.get("available")));
        }

        if (criteria.isVegetarianOnly()) {
            spec = spec.and((root, query, cb) ->
                    cb.isTrue(root.get("vegetarian")));
        }

        if (criteria.getServingPeriod() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("servingPeriod"), criteria.getServingPeriod()));
        }

        if (!CollectionUtils.isEmpty(criteria.getAllergenExclusions())) {
            spec = spec.and((root, query, cb) ->
                    cb.isEmpty(root.get("allergens")));
        }

        return menuItemRepository.findAll(spec).stream()
                .map(menuItemMapper::toDTO)
                .collect(Collectors.toList());
    }
}