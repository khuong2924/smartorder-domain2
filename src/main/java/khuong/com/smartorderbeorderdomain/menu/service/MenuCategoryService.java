//package khuong.com.smartorderbeorderdomain.menu.service;
//
//import jakarta.transaction.Transactional;
//import khuong.com.smartorderbeorderdomain.menu.dto.MenuCategoryDTO;
//import khuong.com.smartorderbeorderdomain.menu.entity.MenuCategory;
//import khuong.com.smartorderbeorderdomain.menu.repository.MenuCategoryRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.common.errors.DuplicateResourceException;
//import org.apache.kafka.common.errors.ResourceNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class MenuCategoryService {
//    private final MenuCategoryRepository categoryRepository;
//    private final MenuCategoryMapper categoryMapper;
//    private final CacheService cacheService;
//
//    @Transactional(readOnly = true)
//    public List<MenuCategoryDTO> getAllCategories() {
//        return categoryRepository.findAllByOrderByDisplayOrder()
//                .stream()
//                .map(categoryMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public MenuCategoryDTO createCategory(CreateMenuCategoryRequest request) {
//        if (categoryRepository.existsByName(request.getName())) {
//            throw new DuplicateResourceException("Category already exists with name: " + request.getName());
//        }
//
//        MenuCategory category = MenuCategory.builder()
//                .name(request.getName())
//                .description(request.getDescription())
//                .displayOrder(request.getDisplayOrder())
//                .active(true)
//                .build();
//
//        MenuCategory savedCategory = categoryRepository.save(category);
//        MenuCategoryDTO dto = categoryMapper.toDTO(savedCategory);
//        cacheService.cacheMenuCategory(dto);
//
//        return dto;
//    }
//
//    @Transactional
//    public MenuCategoryDTO updateCategory(Long id, UpdateMenuCategoryRequest request) {
//        MenuCategory category = categoryRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
//
//        if (request.getName() != null) {
//            category.setName(request.getName());
//        }
//        if (request.getDescription() != null) {
//            category.setDescription(request.getDescription());
//        }
//        if (request.getDisplayOrder() != null) {
//            category.setDisplayOrder(request.getDisplayOrder());
//        }
//        if (request.getActive() != null) {
//            category.setActive(request.getActive());
//        }
//
//        MenuCategory updatedCategory = categoryRepository.save(category);
//        MenuCategoryDTO dto = categoryMapper.toDTO(updatedCategory);
//        cacheService.cacheMenuCategory(dto);
//
//        return dto;
//    }
//
//    @Transactional
//    public void deleteCategory(Long id) {
//        MenuCategory category = categoryRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
//
//        if (!category.getMenuItems().isEmpty()) {
//            throw new BusinessLogicException("Cannot delete category with menu items", "CATEGORY_NOT_EMPTY");
//        }
//
//        categoryRepository.delete(category);
//        cacheService.invalidateMenuCategory(id);
//    }
//}