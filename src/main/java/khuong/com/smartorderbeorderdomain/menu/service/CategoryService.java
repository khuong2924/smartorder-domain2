package khuong.com.smartorderbeorderdomain.menu.service;

import jakarta.persistence.Cacheable;
import khuong.com.smartorderbeorderdomain.menu.dto.response.CategoryResponse;
import khuong.com.smartorderbeorderdomain.menu.dto.request.CreateCategoryRequest;
import khuong.com.smartorderbeorderdomain.menu.dto.request.UpdateCategoryRequest;
import khuong.com.smartorderbeorderdomain.menu.entity.Category;
import khuong.com.smartorderbeorderdomain.menu.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.DuplicateResourceException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Creating new category with name: {}", request.getName());
        validateCategoryName(request.getName());

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder())
                .active(true)
                .build();

        return menuMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = getCategoryEntity(id);

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            validateCategoryName(request.getName());
            category.setName(request.getName());
        }

        updateCategoryFields(category, request);
        return menuMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Cacheable(value = "categories", key = "#id")
    public CategoryResponse getCategoryById(Long id) {
        return menuMapper.toCategoryResponse(getCategoryEntity(id));
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findByActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(menuMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    private Category getCategoryEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    private void validateCategoryName(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Category name already exists");
        }
    }

    private void updateCategoryFields(Category category, UpdateCategoryRequest request) {
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }
    }
}