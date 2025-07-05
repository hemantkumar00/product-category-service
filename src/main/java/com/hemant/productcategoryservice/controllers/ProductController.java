package com.hemant.productcategoryservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hemant.productcategoryservice.dtos.CategoryResponseDto;
import com.hemant.productcategoryservice.dtos.GetAllProductResponseDto;
import com.hemant.productcategoryservice.dtos.InventoryUpdateProducts;
import com.hemant.productcategoryservice.dtos.ProductResponseDto;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody Product product) throws JsonProcessingException {
        Product createdProduct = productService.createProduct(product);
        ProductResponseDto productResponseDto = from(createdProduct);
        return ResponseEntity.ok(productResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getSingleProduct(@PathVariable("id") Long productId){
        Product product = productService.getSingleProduct(productId);
        ProductResponseDto productResponseDto = from(product);
        return ResponseEntity.ok(productResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable("id") Long productId, @RequestBody Product product) throws JsonProcessingException {
        Product updatedProduct = productService.updateProduct(productId, product);
        ProductResponseDto productResponseDto = from(updatedProduct);
        return ResponseEntity.ok(productResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long productId) throws JsonProcessingException {
        boolean deleteResponse = productService.deleteProduct(productId);
        return ResponseEntity.ok(deleteResponse);
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<Integer> getProductInventory(@PathVariable("id") Long productId) {
        int inventoryCount = productService.getProductInventory(productId);
        return ResponseEntity.ok(inventoryCount);
    }

    @PatchMapping("/inventory")
    public ResponseEntity<Boolean> updateProductInventory(@RequestBody InventoryUpdateProducts inventoryUpdateProducts) {
        boolean updateResponse = productService.updateProductInventory(inventoryUpdateProducts.getProducts());
        return ResponseEntity.ok(updateResponse);
    }

    @GetMapping("/")
    public ResponseEntity<GetAllProductResponseDto> getAllProducts(@RequestParam ("page") Integer pageNumber, @RequestParam ("size") Integer pageSize) {
        Page<Product> products = productService.getAllProducts(pageNumber, pageSize);
        GetAllProductResponseDto responseDto = new GetAllProductResponseDto();
        responseDto.setProducts(products.map(ProductController::from));
        responseDto.setCurrentPage(products.getNumber() + 1);
        responseDto.setTotalPages(products.getTotalPages());
        responseDto.setTotalElements(products.getTotalElements());
        return ResponseEntity.ok(responseDto);
    }

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
