package khuong.com.smartorder_domain2.menu.service;

import khuong.com.smartorder_domain2.configs.cloudinary.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import khuong.com.smartorder_domain2.menu.dto.exception.DuplicateResourceException;
import khuong.com.smartorder_domain2.menu.dto.exception.ResourceNotFoundException;
import khuong.com.smartorder_domain2.menu.dto.request.CreateMenuItemRequest;
import khuong.com.smartorder_domain2.menu.dto.request.UpdateMenuItemRequest;
import khuong.com.smartorder_domain2.menu.dto.request.CreateMenuItemWithUrlRequest;
import khuong.com.smartorder_domain2.menu.dto.response.MenuItemResponse;
import khuong.com.smartorder_domain2.menu.entity.Category;
import khuong.com.smartorder_domain2.menu.entity.MenuItem;

import khuong.com.smartorder_domain2.menu.repository.CategoryRepository;
import khuong.com.smartorder_domain2.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuItemService {
    @Autowired
    private final MenuItemRepository menuItemRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    private final PriceHistoryService priceHistoryService;
    @Autowired
    private ImageUploadService imageUploadService;


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



//    @Cacheable(value = "menuItems", key = "#id")
    public MenuItemResponse getMenuItemById(Long id) {
        return MenuItemResponse.fromEntity(findMenuItemById(id));
    }

//    public Page<MenuItemResponse> getAllMenuItems(Pageable pageable) {
//        return menuItemRepository.findByActiveAndAvailable(true, true, pageable)
//                .map(MenuItemResponse::fromEntity);
//    }

    public MenuItem toEntity(MenuItemResponse response) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(response.getId());
        menuItem.setName(response.getName());
        menuItem.setDescription(response.getDescription());
        menuItem.setPrice(response.getPrice());
        menuItem.setPreparationTime(response.getPreparationTime());
        menuItem.setImageUrl(response.getImageUrl());
        return menuItem;
    }

    public List<MenuItemResponse> searchMenuItems(String keyword) {
        return menuItemRepository.searchMenuItems(keyword).stream()
                .map(MenuItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<MenuItemResponse> getAllMenuItems(Pageable pageable) {
        Page<MenuItem> menuItems = menuItemRepository.findAll(pageable);
        return menuItems.map(MenuItemResponse::fromEntity);
    }

    @Transactional
    public void updateMenuItemAvailability(Long menuItemId, boolean available) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + menuItemId));
        
        menuItem.setAvailable(available);
        menuItemRepository.save(menuItem);
        
        log.info("Updated menu item {} availability to: {}", menuItem.getName(), available);
        // You can add WebSocket notification here if needed
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
        if (request.getDescription() != null) {
            menuItem.setDescription(request.getDescription());
        }
        if (request.getPreparationTime() != null) {
            menuItem.setPreparationTime(request.getPreparationTime());
        }
        if (request.getImageUrl() != null) {
            menuItem.setImageUrl(request.getImageUrl());
        }
    }


//    @CacheEvict(cacheNames = CacheConstants.MENU_ITEM_CACHE, allEntries = true)
//    @Transactional
//    public MenuItemResponse createMenuItem(CreateMenuItemRequest request) {
//        Category category = categoryRepository.findById(request.getCategoryId())
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
//
//        validateMenuItemName(request.getName(), request.getCategoryId());
//
//        MenuItem menuItem = MenuItem.builder()
//                .category(category)
//                .name(request.getName())
//                .description(request.getDescription())
//                .price(request.getPrice())
//                .preparationTime(request.getPreparationTime())
//                .imageUrl(request.getImageUrl())
//                .vegetarian(request.isVegetarian())
//                .spicy(request.isSpicy())
//                .calories(request.getCalories())
//                .allergens(request.getAllergens())
//                .available(true)
//                .active(true)
//                .build();
//
//        menuItem = menuItemRepository.save(menuItem);
//        priceHistoryService.saveInitialPrice(menuItem);
//
//        return MenuItemResponse.fromEntity(menuItem);
//    }


    @Transactional
    public MenuItemResponse createMenuItem(CreateMenuItemRequest request) throws IOException, IOException {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        validateMenuItemName(request.getName(), request.getCategoryId());

        MenuItem menuItem = MenuItem.builder()
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .preparationTime(request.getPreparationTime())

                .available(true)
                .active(true)
                .build();

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = imageUploadService.uploadImage(request.getImage());
            menuItem.setImageUrl(imageUrl);
        } else {
            menuItem.setImageUrl("https://www.svgrepo.com/show/508699/landscape-placeholder.svg");
        }

        menuItem = menuItemRepository.save(menuItem);
        priceHistoryService.saveInitialPrice(menuItem);

        return MenuItemResponse.fromEntity(menuItem);
    }

    @Transactional
    public MenuItemResponse createMenuItemWithUrl(CreateMenuItemWithUrlRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        validateMenuItemName(request.getName(), request.getCategoryId());

        MenuItem menuItem = MenuItem.builder()
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .preparationTime(request.getPreparationTime())
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl() : 
                        "https://www.svgrepo.com/show/508699/landscape-placeholder.svg")
                .available(true)
                .active(true)
                .build();

        menuItem = menuItemRepository.save(menuItem);
        priceHistoryService.saveInitialPrice(menuItem);

        return MenuItemResponse.fromEntity(menuItem);
    }

}
