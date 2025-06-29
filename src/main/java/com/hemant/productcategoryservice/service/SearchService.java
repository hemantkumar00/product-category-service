package com.hemant.productcategoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemant.productcategoryservice.dtos.FilterDto;
import com.hemant.productcategoryservice.dtos.SortingCriteria;
import com.hemant.productcategoryservice.exceptions.NoProductsFoundException;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.repositories.ProductRepository;
import com.hemant.productcategoryservice.service.filteringService.FilterFactory;
import com.hemant.productcategoryservice.service.sortingService.SorterFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PRODUCTS_CACHE_KEY = "PRODUCTS";
    private final ObjectMapper objectMapper;
    private static final Duration CACHE_TTL = Duration.ofDays(2);


    public SearchService(ProductRepository productRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public Page<Product> search(String query, List<FilterDto> filters, SortingCriteria sortingCriteria, int pageSize, int pageNumber) throws JsonProcessingException {

        // Fetch products from cache first
        String filtersKey = filters.stream()
                .map(f -> f.getKey() + "=" + String.join(",", f.getValues()))
                .collect(Collectors.joining("&"));

        String cacheKey = "SEARCH_" + query + "_" + filtersKey + "_" + sortingCriteria.name() + "_" + pageSize + "_" + pageNumber;
        String fullCacheKey = PRODUCTS_CACHE_KEY + "_" + cacheKey;
        String cachedJson = (String) redisTemplate.opsForValue().get(fullCacheKey);
        if (cachedJson != null) {
            return objectMapper.readValue(cachedJson, new TypeReference<PageImpl<Product>>() {});
        }

        List<Product> products = productRepository.findByTitleContaining(query);
        if(products.isEmpty()) {
            throw new NoProductsFoundException("No products found for the query: " + query);
        }

        for(FilterDto filter : filters) {
            products = FilterFactory.getFilterFromKey(filter.getKey()).apply(products, filter.getValues());
        }
        products = SorterFactory.getSortedByValue(sortingCriteria).sort(products);
        int start = pageSize * (pageNumber - 1);
        int end = Math.min(start + pageSize, products.size());
        if (start >= products.size()) {
            return Page.empty();
        }
        List<Product> productsOnPage = products.subList(start, end);
        Page<Product> productPage = new PageImpl<>(productsOnPage, PageRequest.of(pageNumber - 1, pageSize), products.size());

        // Store the result in Redis cache
        String json = objectMapper.writeValueAsString(productPage);
        redisTemplate.opsForValue().set(fullCacheKey, json, CACHE_TTL);

        return productPage;
    }

    public Page<Product> simpleSearch(String query, Long categoryId, int pageSize, int pageNumber, String sortingAttribute) throws JsonProcessingException {

        // Fetch products from cache first

        String cacheKey = "SIMPLE_SEARCH_" + query + "_" + categoryId + "_" + sortingAttribute + "_" + pageSize + "_" + pageNumber;
        String fullCacheKey = PRODUCTS_CACHE_KEY + "_" + cacheKey;
        String cachedJson = (String) redisTemplate.opsForValue().get(fullCacheKey);
        if (cachedJson != null) {
            return objectMapper.readValue(cachedJson, new TypeReference<PageImpl<Product>>() {
            });
        }

        Page<Product> productPage =  productRepository
            .findAllByTitleContainingAndCategory_Id(query,
            categoryId,
            PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortingAttribute)));

        String json = objectMapper.writeValueAsString(productPage);
        redisTemplate.opsForValue().set(fullCacheKey, json, CACHE_TTL);

        return productPage;
    }
}
