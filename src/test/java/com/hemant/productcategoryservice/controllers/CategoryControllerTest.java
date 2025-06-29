package com.hemant.productcategoryservice.controllers;

import com.hemant.productcategoryservice.dtos.CategoryResponseDto;
import com.hemant.productcategoryservice.dtos.GetAllCategoriesResponseDto;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CategoryControllerTest {

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private CategoryController categoryController;

    @Test
    public void testCreateCategory_Success() {
        // Arrange
        String categoryName = "Electronics";
        Category category = new Category();
        category.setTitle(categoryName);
        when(categoryService.createCategory(category)).thenReturn(category);
        // Act
        ResponseEntity<CategoryResponseDto> response = categoryController.createCategory(category);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryName, response.getBody().getTitle());
    }

    @Test
    public void testCreateCategory_Failure() {
        // Arrange
        String categoryName = "Electronics";
        Category category = new Category();
        category.setTitle(categoryName);
        when(categoryService.createCategory(category)).thenThrow(new RuntimeException("Category creation failed"));
        // Act & Assert
        try {
            categoryController.createCategory(category);
        } catch (RuntimeException e) {
            assertEquals("Category creation failed", e.getMessage());
        }
    }

    @Test
    public void testGetSingleCategory_Success() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setTitle("Electronics");
        when(categoryService.getSingleCategory(categoryId)).thenReturn(category);
        // Act
        ResponseEntity<CategoryResponseDto> response = categoryController.getSingleCategory(categoryId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getTitle());
    }
    @Test
    public void testGetSingleCategory_Failure() {
        // Arrange
        Long categoryId = 1L;
        when(categoryService.getSingleCategory(categoryId)).thenThrow(new RuntimeException("Category not found"));
        // Act & Assert
        try {
            categoryController.getSingleCategory(categoryId);
        } catch (RuntimeException e) {
            assertEquals("Category not found", e.getMessage());
        }
    }

    @Test
    public void testGetAllCategories_Success() {
        Page<Category> categoryPage = org.springframework.data.domain.Page.empty();
        when(categoryService.getAllCategories(0, 2)).thenReturn(categoryPage);
        // Act
        ResponseEntity<GetAllCategoriesResponseDto> response = categoryController.getAllCategories(0, 2);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllCategories_Failure() {
        when(categoryService.getAllCategories(0, 2)).thenThrow(new RuntimeException("Failed to fetch categories"));
        // Act & Assert
        try {
            categoryController.getAllCategories(0, 2);
        } catch (RuntimeException e) {
            assertEquals("Failed to fetch categories", e.getMessage());
        }
    }

    @Test
    public void testUpdateCategory_Success() {
        // Arrange
        Long categoryId = 1L;
        String updatedCategoryName = "Updated Electronics";
        Category category = new Category();
        category.setId(categoryId);
        category.setTitle(updatedCategoryName);
        when(categoryService.updateCategory(categoryId, category)).thenReturn(category);
        // Act
        ResponseEntity<CategoryResponseDto> response = categoryController.updateCategory(categoryId, category);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCategoryName, response.getBody().getTitle());
    }

    @Test
    public void testUpdateCategory_Failure() {
        // Arrange
        Long categoryId = 1L;
        String updatedCategoryName = "Updated Electronics";
        Category category = new Category();
        category.setId(categoryId);
        category.setTitle(updatedCategoryName);
        when(categoryService.updateCategory(categoryId, category)).thenThrow(new RuntimeException("Category update failed"));
        // Act & Assert
        try {
            categoryController.updateCategory(categoryId, category);
        } catch (RuntimeException e) {
            assertEquals("Category update failed", e.getMessage());
        }
    }

    @Test
    public void testDeleteCategory_Success() {
        // Arrange
        Long categoryId = 1L;
        when(categoryService.deleteCategory(categoryId)).thenReturn(true);
        // Act
        ResponseEntity<Boolean> response = categoryController.deleteCategory(categoryId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteCategory_Failure() {
        // Arrange
        Long categoryId = 1L;
        when(categoryService.deleteCategory(categoryId)).thenThrow(new RuntimeException("Category deletion failed"));
        // Act & Assert
        try {
            categoryController.deleteCategory(categoryId);
        } catch (RuntimeException e) {
            assertEquals("Category deletion failed", e.getMessage());
        }
    }
}
