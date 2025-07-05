package com.hemant.productcategoryservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemant.productcategoryservice.dtos.InventoryUpdateProducts;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.service.ProductService;
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
import java.util.Map;

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

    @Test
    public void getProductInventory_Success() throws Exception {
        when(productService.getProductInventory(1L)).thenReturn(10);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/inventory/1"))
                .andExpect(status().isOk());
    }
    @Test
    public void getProductInventory_Failure() throws Exception {
        when(productService.getProductInventory(1L)).thenThrow(new RuntimeException("Product not found"));
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/inventory/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateProductInventory_Success() throws Exception {
        when(productService.updateProductInventory(any())).thenReturn(true);
        InventoryUpdateProducts inventoryUpdateProducts = new InventoryUpdateProducts();
        Map<Long, Integer> products = Map.of(1L, 50);
        inventoryUpdateProducts.setProducts(products);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/products/inventory")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inventoryUpdateProducts)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateProductInventory_Failure() throws Exception {
        when(productService.updateProductInventory(any())).thenThrow(new RuntimeException("Product not found"));
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/products/inventory"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getAllProducts_Success() throws Exception {
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

        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productService.getAllProducts(1, 10)).thenReturn(productPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/?page=1&size=10"))
                .andExpect(status().isOk());
    }


    @Test
    public void getAllProducts_Failure() throws Exception {
        when(productService.getAllProducts(1, 10)).thenThrow(new RuntimeException("Error fetching products"));
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/products/?page=1&size=10"))
                .andExpect(status().isInternalServerError());
    }
}