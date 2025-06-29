package com.hemant.productcategoryservice.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerMockMVC {

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createCategory_Success() throws Exception {
        // Arrange
        Category category = new Category();
        category.setTitle("Electronics");
        category.setId(1L);

        when(categoryService.createCategory(any())).thenReturn(category);

        // Act & Assert
        mockMvc.perform(
                MockMvcRequestBuilders.post("/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk());
    }

    @Test
    public void createCategory_Failure() throws Exception {
        // Arrange
        Category category = new Category();
        category.setTitle("Electronics");
        category.setId(1L);

        when(categoryService.createCategory(any())).thenThrow(new RuntimeException("Category creation failed"));

        // Act & Assert
        mockMvc.perform(
                MockMvcRequestBuilders.post("/categories/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getAllCategories_Success() throws Exception {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setTitle("Electronics");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setTitle("Books");

        Page<Category> mockPage = new PageImpl<>(List.of(category1, category2));
        when(categoryService.getAllCategories(0, 2)).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/")
                                .param("pageNumber", "0")
                                .param("pageSize", "2")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void getAllCategories_Failure() throws Exception {
        // Arrange
        when(categoryService.getAllCategories(0, 2)).thenThrow(new RuntimeException("Failed to fetch categories"));

        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/")
                                .param("pageNumber", "0")
                                .param("pageSize", "2")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getSingleCategory_Success() throws Exception {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setTitle("Electronics");

        when(categoryService.getSingleCategory(categoryId)).thenReturn(category);

        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/" + categoryId))
                .andExpect(status().isOk());
    }

    @Test
    public void getSingleCategory_Failure() throws Exception {
        // Arrange
        Long categoryId = 1L;
        when(categoryService.getSingleCategory(categoryId)).thenThrow(new RuntimeException("Category not found"));

        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/categories/" + categoryId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateCategory_Success() throws Exception {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setTitle("Electronics");

        when(categoryService.updateCategory(any(), any())).thenReturn(category);

        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/categories/" + categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCategory_Failure() throws Exception {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setTitle("Electronics");

        when(categoryService.updateCategory(any(), any())).thenThrow(new RuntimeException("Category update failed"));

        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/categories/" + categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteCategory_Success() throws Exception {
        // Arrange
        Long categoryId = 1L;

        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/categories/" + categoryId))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCategory_Failure() throws Exception {
        // Arrange
        Long categoryId = 1L;
        when(categoryService.deleteCategory(categoryId)).thenThrow(new RuntimeException("Category deletion failed"));
        // Act & Assert
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/categories/" + categoryId))
                .andExpect(status().isInternalServerError());
    }

}
