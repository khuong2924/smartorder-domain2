//package khuong.com.smartorderbeorderdomain.menu.service;
//
//
//import khuong.com.smartorderbeorderdomain.menu.dto.MenuItemDTO;
//import khuong.com.smartorderbeorderdomain.menu.entity.MenuCategory;
//import khuong.com.smartorderbeorderdomain.menu.entity.MenuItem;
//import khuong.com.smartorderbeorderdomain.menu.repository.MenuCategoryRepository;
//import khuong.com.smartorderbeorderdomain.menu.repository.MenuItemRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.common.errors.ResourceNotFoundException;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.awt.print.Pageable;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class MenuItemService {
//    private final MenuItemRepository menuItemRepository;
//    private final MenuCategoryRepository categoryRepository;
//    private final MenuItemMapper menuItemMapper;
//    private final CacheService cacheService;
//    private final WebSocketService webSocketService;
//
//    @Transactional(readOnly = true)
//    public Page<MenuItemDTO> getAllMenuItems(Pageable pageable) {
//        return menuItemRepository.findAll(pageable)
//                .map(menuItemMapper::toDTO);
//    }
//
//    @Transactional(readOnly = true)
//    public MenuItemDTO getMenuItem(Long id) {
//        return cacheService.getCachedMenuItem(id)
//                .orElseGet(() -> {
//                    MenuItemDTO dto = menuItemRepository.findById(id)
//                            .map(menuItemMapper::toDTO)
//                            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
//                    cacheService.cacheMenuItem(dto);
//                    return dto;
//                });
//    }
//
//    @Transactional
//    public MenuItemDTO createMenuItem(CreateMenuItemRequest request) {
//        MenuCategory category = categoryRepository.findById(request.getCategoryId())
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
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
//                .displayOrder(request.getDisplayOrder())
//                .available(true)
//                .active(true)
//                .build();
//
//        MenuItem savedItem = menuItemRepository.save(menuItem);
//        MenuItemDTO dto = menuItemMapper.toDTO(savedItem);
//
//        cacheService.cacheMenuItem(dto);
//        webSocketService.notifyMenuUpdate("ITEM_CREATED", dto);
//
//        return dto;
//    }
//
//    @Transactional
//    public MenuItemDTO updateMenuItem(Long id, UpdateMenuItemRequest request) {
//        MenuItem menuItem = menuItemRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
//
//        updateMenuItemFields(menuItem, request);
//
//        MenuItem updatedItem = menuItemRepository.save(menuItem);
//        MenuItemDTO dto = menuItemMapper.toDTO(updatedItem);
//
//        cacheService.cacheMenuItem(dto);
//        webSocketService.notifyMenuUpdate("ITEM_UPDATED", dto);
//
//        return dto;
//    }
//
//    @Transactional
//    public void deleteMenuItem(Long id) {
//        MenuItem menuItem = menuItemRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
//
//        menuItemRepository.delete(menuItem);
//        cacheService.invalidateMenuItem(id);
//        webSocketService.notifyMenuUpdate("ITEM_DELETED", menuItemMapper.toDTO(menuItem));
//    }
//
//    @Transactional
//    public void updateAvailability(Long id, boolean available) {
//        MenuItem menuItem = menuItemRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
//
//        menuItem.setAvailable(available);
//        MenuItem updatedItem = menuItemRepository.save(menuItem);
//
//        MenuItemDTO dto = menuItemMapper.toDTO(updatedItem);
//        cacheService.cacheMenuItem(dto);
//        webSocketService.notifyAvailabilityChange(id, available);
//    }
//
//    private void updateMenuItemFields(MenuItem menuItem, UpdateMenuItemRequest request) {
//        if (request.getName() != null) {
//            menuItem.setName(request.getName());
//        }
//        if (request.getDescription() != null) {
//            menuItem.setDescription(request.getDescription());
//        }
//        if (request.getPrice() != null) {
//            menuItem.setPrice(request.getPrice());
//        }
//        // ... update other fields
//    }
//}