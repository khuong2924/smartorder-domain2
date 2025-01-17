package khuong.com.smartorderbeorderdomain.menu.service;

import jakarta.transaction.Transactional;
import khuong.com.smartorderbeorderdomain.menu.dto.MenuCategoryDTO;
import khuong.com.smartorderbeorderdomain.menu.entity.MenuCategory;
import khuong.com.smartorderbeorderdomain.menu.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.DuplicateResourceException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MenuCategoryService {
    private final MenuCategoryRepository categoryRepository;
    private final MenuCategoryMapper categoryMapper;

    public MenuCategoryDTO createCategory(CreateMenuCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category name already exists: " + request.getName());
        }

        MenuCategory category = new MenuCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setImageUrl(request.getImageUrl());
        category.setCreatedBy(SecurityUtils.getCurrentUserId());

        MenuCategory savedCategory = categoryRepository.save(category);
        log.info("Created new menu category: {}", savedCategory.getName());
        return categoryMapper.toDTO(savedCategory);
    }

    public MenuCategoryDTO updateCategory(Long id, UpdateMenuCategoryRequest request) {
        MenuCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException("Category name already exists: " + request.getName());
            }
            category.setName(request.getName());
        }

        Optional.ofNullable(request.getDescription()).ifPresent(category::setDescription);
        Optional.ofNullable(request.getDisplayOrder()).ifPresent(category::setDisplayOrder);
        Optional.ofNullable(request.getImageUrl()).ifPresent(category::setImageUrl);
        Optional.ofNullable(request.getActive()).ifPresent(category::setActive);

        category.setUpdatedBy(SecurityUtils.getCurrentUserId());
        MenuCategory updatedCategory = categoryRepository.save(category);
        log.info("Updated menu category: {}", updatedCategory.getName());
        return categoryMapper.toDTO(updatedCategory);
    }

    public List<MenuCategoryDTO> getActiveCategories() {
        return categoryRepository.findByActiveTrueOrderByDisplayOrder().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}