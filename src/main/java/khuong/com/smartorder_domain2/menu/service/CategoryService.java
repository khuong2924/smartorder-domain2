package khuong.com.smartorder_domain2.menu.service;

import khuong.com.smartorder_domain2.menu.dto.exception.DuplicateResourceException;
import khuong.com.smartorder_domain2.menu.dto.request.CreateCategoryRequest;
import khuong.com.smartorder_domain2.menu.dto.request.UpdateCategoryRequest;
import khuong.com.smartorder_domain2.menu.dto.response.CategoryResponse;
import khuong.com.smartorder_domain2.menu.entity.Category;
import khuong.com.smartorder_domain2.menu.dto.exception.ResourceNotFoundException;
import khuong.com.smartorder_domain2.menu.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;


    public CategoryResponse getCategoryById(Long id) {
        log.info("Fetching category from database for id: {}", id);
        return CategoryResponse.fromEntity(findCategoryById(id));
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        categories.forEach(category -> Hibernate.initialize(category.getMenuItems()));
        return categories;
    }

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        validateCategoryName(request.getName());

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder())
                .active(true)
                .build();

        category = categoryRepository.save(category);
        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = findCategoryById(id);

        if (request.getName() != null && !request.getName().equals(category.getName())) {
            validateCategoryName(request.getName());
            category.setName(request.getName());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getDisplayOrder() != null) {
            category.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        category = categoryRepository.save(category);
        return CategoryResponse.fromEntity(category);
    }
    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }


    @Transactional
    public void deleteCategory(Long id) {
        Category category = findCategoryById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }

    private void validateCategoryName(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Category already exists with name: " + name);
        }
    }
}