package com.hemant.productcategoryservice.controllers;

import com.hemant.productcategoryservice.dtos.CategoryResponseDto;
import com.hemant.productcategoryservice.dtos.ProductResponseDto;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        ProductResponseDto productResponseDto = from(createdProduct);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getSingleProduct(@PathVariable("id") Long productId) {
        Product product = productService.getSingleProduct(productId);
        ProductResponseDto productResponseDto = from(product);
        return ResponseEntity.ok(productResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable("id") Long productId, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(productId, product);
        ProductResponseDto productResponseDto = from(updatedProduct);
        return ResponseEntity.ok(productResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long productId) {
        boolean deleteResponse = productService.deleteProduct(productId);
        return ResponseEntity.ok(deleteResponse);
    }

    // Search products and get all products and search by filters will be handled by the SearchController

    // converting Product to ProductResponseDto
    public static ProductResponseDto from(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setTitle(product.getTitle());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setCategory(from(product.getCategory()));
        productResponseDto.setImgUrl(product.getImgUrl());
        return productResponseDto;
    }

    // converting Category to CategoryResponseDto
    public static CategoryResponseDto from(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setTitle(category.getTitle());
        return categoryResponseDto;
    }
}
