package khuong.com.smartorderbeorderdomain.menu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository categoryRepository;
    private final KafkaTemplate<String, MenuItemEvent> kafkaTemplate;

    public MenuItemDTO createMenuItem(CreateMenuItemRequest request) {
        MenuCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(category);
        menuItem.setAvailable(true);
        menuItem.setImageUrl(request.getImageUrl());

        MenuItem savedItem = menuItemRepository.save(menuItem);
        return mapToDTO(savedItem);
    }

    public MenuItemDTO updateAvailability(Long id, boolean available) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        menuItem.setAvailable(available);
        MenuItem updatedItem = menuItemRepository.save(menuItem);

        // Notify other services about availability change
        kafkaTemplate.send("menu-items", new MenuItemEvent(
                menuItem.getId(),
                "AVAILABILITY_CHANGED",
                available
        ));

        return mapToDTO(updatedItem);
    }
}