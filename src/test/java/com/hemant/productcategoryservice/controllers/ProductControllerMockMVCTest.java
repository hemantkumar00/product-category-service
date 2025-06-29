package com.hemant.productcategoryservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerMockMVCTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createProduct_Success() throws Exception {
        Product product = new Product();
        product.setTitle("iPhone 16 Pro Max");
        product.setId(1L);
        product.setPrice(100.0);
        product.setDescription("iPhone 16 Pro Max");
        product.setImgUrl("iPhone 16 Pro Max");
        Category category = new Category();
        category.setId(1L);
        category.setTitle("title");
        product.setCategory(category);

        when(productService.createProduct(any())).thenReturn(product);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/products/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    public void createProduct_Failure() throws Exception {
        Product product = new Product();
        product.setTitle("iPhone 16 Pro Max");
        product.setId(1L);
        product.setPrice(100.0);
        product.setDescription("iPhone 16 Pro Max");
        product.setImgUrl("iPhone 16 Pro Max");
        Category category = new Category();
        category.setId(1L);
        category.setTitle("title");
        product.setCategory(category);

        when(productService.createProduct(any())).thenThrow(new RuntimeException("Category not found"));
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/products/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getSingleProduct_Success() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("iPhone 16 Pro Max");
        product.setPrice(100.0);
        product.setDescription("iPhone 16 Pro Max");
        product.setImgUrl("iPhone 16 Pro Max");
        Category category = new Category();
        category.setId(1L);
        category.setTitle("title");
        product.setCategory(category);

        when(productService.getSingleProduct(1L)).thenReturn(product);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getSingleProduct_Failure() throws Exception {
        when(productService.getSingleProduct(1L)).thenThrow(new RuntimeException("Product not found"));
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateProduct_Success() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("iPhone 16 Pro Max");
        product.setPrice(100.0);
        product.setDescription("iPhone 16 Pro Max");
        product.setImgUrl("iPhone 16 Pro Max");
        Category category = new Category();
        category.setId(1L);
        category.setTitle("title");
        product.setCategory(category);

        when(productService.updateProduct(any(), any())).thenReturn(product);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateProduct_Failure() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("iPhone 16 Pro Max");
        product.setPrice(100.0);
        product.setDescription("iPhone 16 Pro Max");
        product.setImgUrl("iPhone 16 Pro Max");
        Category category = new Category();
        category.setId(1L);
        category.setTitle("title");
        product.setCategory(category);

        when(productService.updateProduct(any(), any())).thenThrow(new RuntimeException("Product not found"));
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteProduct_Success() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProduct_Failure() throws Exception {
        when(productService.deleteProduct(1L)).thenThrow(new RuntimeException("Product not found"));
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/products/1"))
                .andExpect(status().isInternalServerError());
    }
}