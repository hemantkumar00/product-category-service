package com.hemant.productcategoryservice.service;

import com.hemant.productcategoryservice.exceptions.CategoryAlreadyExistsException;
import com.hemant.productcategoryservice.exceptions.CategoryDoesNotExistException;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        if (category == null || category.getTitle() == null || category.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Category title cannot be null or empty");
        }
        if (categoryRepository.findByTitle(category.getTitle()).isPresent()) {
            throw new CategoryAlreadyExistsException("Category with title " + category.getTitle() + " already exists");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, Category category) {
        if(category == null){
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (category.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Category title cannot be null or empty");
        }
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryDoesNotExistException("Category with id " + categoryId + " does not exist"));
        existingCategory.setTitle(category.getTitle());
        return categoryRepository.save(existingCategory);
    }

    public boolean deleteCategory(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryDoesNotExistException("Category with id " + categoryId + " does not exist"));
        categoryRepository.deleteById(categoryId);
        return true;
    }

    public Category getSingleCategory(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryDoesNotExistException("Category with id " + categoryId + " does not exist"));
    }

    public Page<Category> getAllCategories(Integer pageNumber, Integer pageSize) {
        return categoryRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }
}
