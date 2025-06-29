package com.hemant.productcategoryservice.service;

import com.hemant.productcategoryservice.exceptions.CategoryDoesNotExistException;
import com.hemant.productcategoryservice.exceptions.ProductDoesNotExistException;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.repositories.CategoryRepository;
import com.hemant.productcategoryservice.repositories.ProductRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PRODUCTS_CACHE_KEY = "PRODUCTS";

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository, RedisTemplate<String, Object> redisTemplate) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }

    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (
                product.getTitle() == null ||
                        product.getTitle().isEmpty() ||
                        product.getPrice() == null ||
                        product.getPrice() <= 0 ||
                        product.getCategory() == null ||
                        product.getCategory().getTitle() == null ||
                        product.getCategory().getTitle().isEmpty() ||
                        product.getDescription() == null ||
                        product.getDescription().isEmpty() ||
                        product.getImgUrl() == null ||
                        product.getImgUrl().isEmpty() ||
                        product.getQuantity() == null ||
                        product.getQuantity() <= 0
        ) {
            throw new IllegalArgumentException("Product title, price, category, description, quantity and image URL cannot be null or empty");
        }
        Category category = categoryRepository.findByTitle(product.getCategory().getTitle())
                .orElseThrow(() -> new CategoryDoesNotExistException("Category with title " + product.getCategory().getTitle() + " does not exist"));
        product.setCategory(category);
        return productRepository.save(product);
    }

    public Product getSingleProduct(Long productId) {

        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        // Check Redis cache first
        String fullCacheKey = PRODUCTS_CACHE_KEY + "_PRODUCT_" + productId;
        Product product = (Product) redisTemplate.opsForValue().get(fullCacheKey);
        if (product != null) {
            return product;
        }

        // If not found in cache, fetch from database
        product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductDoesNotExistException("Product with id " + productId + " does not exist"));

        // Store the product in Redis cache

        redisTemplate.opsForValue().set(fullCacheKey,product, Duration.ofDays(2));
        return product;
    }

    public Product updateProduct(Long productId, Product product) {

        // Remove the product from Redis cache if it exists
        String fullCacheKey = PRODUCTS_CACHE_KEY + "_PRODUCT_" + productId;
        Product cachedProduct = (Product) redisTemplate.opsForValue().get(fullCacheKey);
        if (cachedProduct != null) {
            redisTemplate.delete(fullCacheKey);
        }

        if (productId == null || product == null) {
            throw new IllegalArgumentException("Product ID and product cannot be null");
        }
        if (productRepository.existsById(productId)) {
            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductDoesNotExistException("Product with id " + productId + " does not exist"));
            if (product.getTitle() != null && !product.getTitle().isEmpty()) {
                existingProduct.setTitle(product.getTitle());
            }
            if (product.getDescription() != null && !product.getDescription().isEmpty()) {
                existingProduct.setDescription(product.getDescription());
            }
            if (product.getPrice() != null && product.getPrice() > 0) {
                existingProduct.setPrice(product.getPrice());
            }
            if (product.getImgUrl() != null && !product.getImgUrl().isEmpty()) {
                existingProduct.setImgUrl(product.getImgUrl());
            }
            if (product.getQuantity() != null && product.getQuantity() >= 0) {
                existingProduct.setQuantity(product.getQuantity());
            }
            if (product.getCategory() != null && product.getCategory().getTitle() != null && !product.getCategory().getTitle().isEmpty()) {
                Category category = categoryRepository.findByTitle(product.getCategory().getTitle())
                        .orElseThrow(() -> new CategoryDoesNotExistException("Category with title " + product.getCategory().getTitle() + " does not exist"));
                existingProduct.setCategory(category);
            }
            return productRepository.save(existingProduct);
        }
        throw new ProductDoesNotExistException("Product with id " + productId + " does not exist");
    }

    public boolean deleteProduct(Long productId) {

        // Remove the product from Redis cache if it exists
        String fullCacheKey = PRODUCTS_CACHE_KEY + "_PRODUCT_" + productId;
        Product cachedProduct = (Product) redisTemplate.opsForValue().get(fullCacheKey);
        if (cachedProduct != null) {
            redisTemplate.delete(fullCacheKey);
        }

        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        throw new ProductDoesNotExistException("Product with id " + productId + " does not exist");
    }
}
