package com.hemant.productcategoryservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemant.productcategoryservice.dtos.FilterDto;
import com.hemant.productcategoryservice.dtos.SearchResponseDto;
import com.hemant.productcategoryservice.dtos.SortingCriteria;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;
    private final ObjectMapper objectMapper;
    public SearchController(SearchService searchService, ObjectMapper objectMapper) {
        this.searchService = searchService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public ResponseEntity<SearchResponseDto> search(
            @RequestParam("query") String query,
            @RequestParam("filters") String filtersJson,
            @RequestParam("sortBy") SortingCriteria sortingCriteria,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("pageNumber") int pageNumber
    ) throws JsonProcessingException {
        List<FilterDto> filters = objectMapper.readValue(filtersJson, new TypeReference<>() {});
        Page<Product> productsPage = searchService.search(query, filters, sortingCriteria, pageSize, pageNumber);
        SearchResponseDto response = new SearchResponseDto();
        response.setProducts(productsPage.map(ProductController::from));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/byCategory")
    public ResponseEntity<SearchResponseDto> simpleSearch(
            @RequestParam("query") String query,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("sortingAttribute") String sortingAttribute
    ) throws JsonProcessingException {
        Page<Product> productsPage = searchService.simpleSearch(query, categoryId, pageSize, pageNumber, sortingAttribute);
        SearchResponseDto response = new SearchResponseDto();
        response.setProducts(productsPage.map(ProductController::from));
        return ResponseEntity.ok(response);
    }
}
