package com.hemant.productcategoryservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hemant.productcategoryservice.dtos.GetAllProductResponseDto;
import com.hemant.productcategoryservice.dtos.InventoryUpdateProducts;
import com.hemant.productcategoryservice.dtos.ProductResponseDto;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductControllerTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ProductController productController;

    @Test
    public void testCreateProduct_Success() throws JsonProcessingException {
        // Arrange
        Product product = new Product();
        Category category = new Category();
        category.setId(1L);
        product.setTitle("iPhone 16 Pro Max");
        product.setCategory(category);
        ProductResponseDto expectedProduct = new ProductResponseDto();
        expectedProduct.setTitle("iPhone 16 Pro Max");
        when(productService.createProduct(product)).thenReturn(product);
        // Act
        ResponseEntity<ProductResponseDto> response = productController.createProduct(product);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProduct.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void testCreateProduct_Failure() throws JsonProcessingException {
        // Arrange
        Product product = new Product();
        product.setTitle("iPhone 16 Pro Max");
        when(productService.createProduct(product)).thenThrow(new RuntimeException("Category not found"));

        // Act & Assert
        try {
            productController.createProduct(product);
        } catch (RuntimeException e) {
            assertEquals("Category not found", e.getMessage());
        }
    }

    @Test
    public void testGetSingleProduct_Success(){
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setTitle("iPhone 16 Pro Max");
        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);
        ProductResponseDto expectedProductResponse = ProductController.from(product);
        when(productService.getSingleProduct(productId)).thenReturn(product);
        // Act
        ResponseEntity<ProductResponseDto> response = productController.getSingleProduct(productId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProductResponse.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void testGetSingleProduct_Failure(){
        // Arrange
        Long productId = 1L;
        when(productService.getSingleProduct(productId)).thenThrow(new RuntimeException("Product not found"));
        // Act & Assert
        try {
            productController.getSingleProduct(productId);
        } catch (RuntimeException e) {
            assertEquals("Product not found", e.getMessage());
        }
    }

    @Test
    public void testUpdateProduct_Success() throws JsonProcessingException {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setTitle("iPhone 16 Pro Max");
        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);
        ProductResponseDto expectedProductResponse = ProductController.from(product);
        when(productService.updateProduct(productId, product)).thenReturn(product);
        // Act
        ResponseEntity<ProductResponseDto> response = productController.update(productId, product);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProductResponse.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void testUpdateProduct_Failure() throws JsonProcessingException {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setTitle("iPhone 16 Pro Max");
        when(productService.updateProduct(productId, product)).thenThrow(new RuntimeException("Product not found"));
        // Act & Assert
        try {
            productController.update(productId, product);
        } catch (RuntimeException e) {
            assertEquals("Product not found", e.getMessage());
        }
    }

    @Test
    public void testDeleteProduct_Success() throws JsonProcessingException {
        // Arrange
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenReturn(true);
        // Act
        ResponseEntity<Boolean> response = productController.deleteProduct(productId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testDeleteProduct_Failure() throws JsonProcessingException {
        // Arrange
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenThrow(new RuntimeException("Product not found"));
        // Act & Assert
        try {
            productController.deleteProduct(productId);
        } catch (RuntimeException e) {
            assertEquals("Product not found", e.getMessage());
        }
    }

    @Test
    public void testGetProductInventory_Success() {
        // Arrange
        Long productId = 1L;
        int inventoryCount = 100;
        when(productService.getProductInventory(productId)).thenReturn(inventoryCount);
        // Act
        ResponseEntity<Integer> response = productController.getProductInventory(productId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inventoryCount, response.getBody().intValue());
    }

    @Test
    public void testGetProductInventory_Failure() {
        // Arrange
        Long productId = 1L;
        when(productService.getProductInventory(productId)).thenThrow(new RuntimeException("Product not found"));
        // Act & Assert
        try {
            productController.getProductInventory(productId);
        } catch (RuntimeException e) {
            assertEquals("Product not found", e.getMessage());
        }
    }

    @Test
    public void testUpdateProductInventory_Success() {
        // Arrange
        Long productId = 1L;
        int quantity = 50;
        when(productService.updateProductInventory(any())).thenReturn(true);
        InventoryUpdateProducts inventoryUpdateProducts = new InventoryUpdateProducts();
        Map<Long, Integer> products = Map.of(productId, quantity);
        inventoryUpdateProducts.setProducts(products);
        // Act
        ResponseEntity<Boolean> response = productController.updateProductInventory(inventoryUpdateProducts);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testUpdateProductInventory_Failure() {
        // Arrange
        Long productId = 1L;
        int quantity = 50;
        when(productService.updateProductInventory(any())).thenThrow(new RuntimeException("Product not found"));
        InventoryUpdateProducts inventoryUpdateProducts = new InventoryUpdateProducts();
        Map<Long, Integer> products = Map.of(productId, quantity);
        inventoryUpdateProducts.setProducts(products);
        // Act & Assert
        try {
            productController.updateProductInventory(inventoryUpdateProducts);
        } catch (RuntimeException e) {
            assertEquals("Product not found", e.getMessage());
        }
    }

    @Test
    public void testGetAllProducts_Success() {
        // Arrange
        int pageNumber = 1;
        int pageSize = 10;
        Page<Product> products = Page.empty(); // Mocking an empty page of products
        when(productService.getAllProducts(pageNumber, pageSize)).thenReturn(products);// Mocking a list of products
        // Act
        ResponseEntity<GetAllProductResponseDto> response = productController.getAllProducts(pageNumber, pageSize);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllProducts_Failure() {
        // Arrange
        int pageNumber = 1;
        int pageSize = 10;
        when(productService.getAllProducts(pageNumber, pageSize)).thenThrow(new RuntimeException("Error fetching products"));
        // Act & Assert
        try {
            productController.getAllProducts(pageNumber, pageSize);
        } catch (RuntimeException e) {
            assertEquals("Error fetching products", e.getMessage());
        }
    }
}
