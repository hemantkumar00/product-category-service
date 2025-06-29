package com.hemant.productcategoryservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemant.productcategoryservice.dtos.FilterDto;
import com.hemant.productcategoryservice.dtos.SearchResponseDto;
import com.hemant.productcategoryservice.dtos.SortingCriteria;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SearchControllerTest {

    @Autowired
    private SearchController searchController;

    @MockitoBean
    private SearchService searchService;


    @Test
    public void simpleSearchTest_Success() throws JsonProcessingException {
        Page<Product> categoryPage = org.springframework.data.domain.Page.empty();
        when(searchService.simpleSearch("hello", 1L, 0, 2, "sort" )).thenReturn(categoryPage);
        // Act
        ResponseEntity<SearchResponseDto> response = searchController.simpleSearch("hello", 1L, 0, 2, "sort");
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void simpleSearchTest_Failure() throws JsonProcessingException {
        // Arrange
        when(searchService.simpleSearch("hello", 1L, 0, 2, "sort")).thenThrow(new RuntimeException("Search failed"));
        // Act & Assert
        try {
            searchController.simpleSearch("hello", 1L, 0, 2, "sort");
        } catch (RuntimeException e) {
            assertEquals("Search failed", e.getMessage());
        }
    }

    @Test
    public void searchTest_Success() throws JsonProcessingException {
        Page<Product> categoryPage = org.springframework.data.domain.Page.empty();
        when(searchService.search("hello", List.of(), SortingCriteria.PRICE_HIGH_TO_LOW, 0, 2)).thenReturn(categoryPage);
        List<FilterDto> filters = List.of();
        String filterJson = new ObjectMapper().writeValueAsString(filters);
        // Act
        ResponseEntity<SearchResponseDto> response = searchController.search("hello", filterJson, SortingCriteria.PRICE_HIGH_TO_LOW, 0, 2);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void searchTest_Failure() throws JsonProcessingException {

        List<FilterDto> filters = List.of();
        String filterJson = new ObjectMapper().writeValueAsString(filters);
        // Arrange
        when(searchService.search("hello", List.of(), SortingCriteria.PRICE_HIGH_TO_LOW, 0, 2)).thenThrow(new RuntimeException("Search failed"));
        // Act & Assert
        try {
            searchController.search("hello", filterJson, SortingCriteria.PRICE_HIGH_TO_LOW, 0, 2);
        } catch (RuntimeException e) {
            assertEquals("Search failed", e.getMessage());
        }
    }
}
