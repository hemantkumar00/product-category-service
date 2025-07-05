package com.hemant.productcategoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemant.productcategoryservice.configs.KafkaProducerClientConfig;
import com.hemant.productcategoryservice.dtos.OperationType;
import com.hemant.productcategoryservice.dtos.ProductPaymentCreationDto;
import com.hemant.productcategoryservice.exceptions.CategoryDoesNotExistException;
import com.hemant.productcategoryservice.exceptions.ProductDoesNotExistException;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.repositories.CategoryRepository;
import com.hemant.productcategoryservice.repositories.ProductRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PRODUCTS_CACHE_KEY = "PRODUCTS";
    private final KafkaProducerClientConfig kafkaProducerClientConfig;
    private final ObjectMapper objectMapper;

    //this is to create product in payment service as I am using strip for now.
    private final String PRODUCT_CREATED_TOPIC = "product_created_create_product_in_payment_service";

    public ProductService(CategoryRepository categoryRepository,
                          ProductRepository productRepository,
                          RedisTemplate<String, Object> redisTemplate,
                          RedissonClient redissonClient,
                          KafkaProducerClientConfig kafkaProducerClientConfig,
                          ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.kafkaProducerClientConfig = kafkaProducerClientConfig;
        this.objectMapper = objectMapper;
    }

    public Product createProduct(Product product) throws JsonProcessingException {
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
        Product finalProduct = product;
        Category category = categoryRepository.findByTitle(product.getCategory().getTitle())
                .orElseThrow(() -> new CategoryDoesNotExistException("Category with title " + finalProduct.getCategory().getTitle() + " does not exist"));
        product.setCategory(category);
        product = productRepository.save(product);

        ProductPaymentCreationDto productPaymentCreationDto = new ProductPaymentCreationDto();
        productPaymentCreationDto.setProductId(product.getId());
        productPaymentCreationDto.setPrice(product.getPrice());
        productPaymentCreationDto.setTitle(product.getTitle());
        productPaymentCreationDto.setDescription(product.getDescription());
        productPaymentCreationDto.setOperationType(OperationType.ADD);

        kafkaProducerClientConfig.sendMessage(PRODUCT_CREATED_TOPIC, objectMapper.writeValueAsString(productPaymentCreationDto));

        return product;
    }

    public Product getSingleProduct(Long productId){

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

    public Product updateProduct(Long productId, Product product) throws JsonProcessingException {

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
                Product finalProduct = product;
                Category category = categoryRepository.findByTitle(product.getCategory().getTitle())
                        .orElseThrow(() -> new CategoryDoesNotExistException("Category with title " + finalProduct.getCategory().getTitle() + " does not exist"));
                existingProduct.setCategory(category);
            }

            product = productRepository.save(product);

            ProductPaymentCreationDto productPaymentCreationDto = new ProductPaymentCreationDto();
            productPaymentCreationDto.setProductId(product.getId());
            productPaymentCreationDto.setPrice(product.getPrice());
            productPaymentCreationDto.setTitle(product.getTitle());
            productPaymentCreationDto.setDescription(product.getDescription());
            productPaymentCreationDto.setOperationType(OperationType.UPDATE);

            kafkaProducerClientConfig.sendMessage(PRODUCT_CREATED_TOPIC, objectMapper.writeValueAsString(productPaymentCreationDto));

            return productRepository.save(existingProduct);
        }
        throw new ProductDoesNotExistException("Product with id " + productId + " does not exist");
    }

    public boolean deleteProduct(Long productId) throws JsonProcessingException {

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
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductDoesNotExistException("Product with id " + productId + " does not exist"));
            productRepository.deleteById(productId);
            product = productRepository.save(product);

            ProductPaymentCreationDto productPaymentCreationDto = new ProductPaymentCreationDto();
            productPaymentCreationDto.setProductId(product.getId());
            productPaymentCreationDto.setPrice(product.getPrice());
            productPaymentCreationDto.setTitle(product.getTitle());
            productPaymentCreationDto.setDescription(product.getDescription());
            productPaymentCreationDto.setOperationType(OperationType.REMOVE);

            kafkaProducerClientConfig.sendMessage(PRODUCT_CREATED_TOPIC, objectMapper.writeValueAsString(productPaymentCreationDto));
            return true;
        }

        throw new ProductDoesNotExistException("Product with id " + productId + " does not exist");
    }

    public int getProductInventory(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductDoesNotExistException("Product with id " + productId + " does not exist"));
        if (product.getQuantity() != null) {
            return product.getQuantity();
        }
        return 0;
    }

    public boolean updateProductInventory(Map<Long, Integer> products) {
        List<Long> sortedProductIds = new ArrayList<>(products.keySet());
        Collections.sort(sortedProductIds);
        List<RLock> locks = new ArrayList<>();
        Map<Long, Integer> deductedQuantitiesMap = new HashMap<>();

        try{

            // Step 1: Acquire locks for each product in sorted order
            for (Long productId : sortedProductIds) {
                RLock lock = redissonClient.getLock("lock:PRODUCT_INVENTORY:" + productId);
                boolean isLocked = lock.tryLock(10, TimeUnit.SECONDS);
                if (!isLocked) {
                    throw new RuntimeException("Could not acquire lock for product ID: " + productId);
                }
                locks.add(lock);
            }

            // Step 2: Deduct quantities for each product

            for(Long productId : sortedProductIds) {
                Integer quantityToDeduct = products.get(productId);
                if (quantityToDeduct == null || quantityToDeduct <= 0) {
                    throw new IllegalArgumentException("Invalid quantity for product ID: " + productId);
                }
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ProductDoesNotExistException("Product with id " + productId + " does not exist"));
                if (product.getQuantity() == null || product.getQuantity() < quantityToDeduct) {
                    throw new IllegalArgumentException("Insufficient inventory for product ID: " + productId);
                }
                int newQuantity = product.getQuantity() - quantityToDeduct;
                product.setQuantity(newQuantity);
                productRepository.save(product);
                deductedQuantitiesMap.put(productId, quantityToDeduct);
            }
            return true;
        }catch (Exception e){

            // step 3: Rollback changes if any exception occurs
            for (Long productId : deductedQuantitiesMap.keySet()) {
                Product product = productRepository.findById(productId).orElse(null);
                if (product != null) {
                    int quantityToAddBack = deductedQuantitiesMap.get(productId);
                    product.setQuantity(product.getQuantity() + quantityToAddBack);
                    productRepository.save(product);
                }
            }
            return false;
        }finally {
            // Step 4: Release locks
            Collections.reverse(locks);
            for (RLock lock : locks) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    public Page<Product> getAllProducts(Integer pageNumber, Integer pageSize) {
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or size");
        }
        return productRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }
}
