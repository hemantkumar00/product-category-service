package com.hemant.productcategoryservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemant.productcategoryservice.dtos.FilterDto;
import com.hemant.productcategoryservice.dtos.SortingCriteria;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.models.Product;
import com.hemant.productcategoryservice.service.SearchService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
public class SearchControllerMockMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SearchService searchService;

    @Test
    public void simpleSearch_ByCategory_Success() throws Exception {
        when(searchService.simpleSearch("test", 1L, 10, 0, "price,asc"))
                .thenReturn(Page.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/search/byCategory")
                                .param("query", "test")
                                .param("categoryId", "1")
                                .param("pageSize", "10")
                                .param("pageNumber", "0")
                                .param("sortingAttribute", "price,asc"))
                .andExpect(status().isOk());
    }

    @Test
    public void fullSearch_WithFilters_Success() throws Exception {
        FilterDto filter = new FilterDto();
        filter.setKey("brand");
        filter.setValues(List.of("apple", "samsung"));
        Product product = new Product();
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Electronics");
        product.setId(1L);
        product.setTitle("Test Product");
        product.setImgUrl("https://www.baidu.com");
        product.setDescription("test product");
        product.setPrice(100.0);
        product.setCategory(category);

        Page<Product> productPage = new PageImpl<>(List.of(product));

        String filtersJson = objectMapper.writeValueAsString(List.of(filter));

        when(searchService.search(any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(productPage); // ✅ Important: avoid null here

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/search/")
                                .param("query", "test")
                                .param("filters", filtersJson)
                                .param("sortBy", SortingCriteria.PRICE_HIGH_TO_LOW.name())
                                .param("pageSize", "10")
                                .param("pageNumber", "0")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }




    @Test
    public void simpleSearch_ByCategory_MissingParam_Failure() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/search/byCategory")
                                .param("query", "test")
                                // categoryId is missing
                                .param("pageSize", "10")
                                .param("pageNumber", "0")
                                .param("sortingAttribute", "price,asc"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void fullSearch_InvalidSortingCriteria_Failure() throws Exception {
        // Invalid JSON string for SortingCriteria (not deserializable)
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/search/")
                                .param("query", "test")
                                .param("filters", "[]")
                                .param("sortBy", "invalid") // This should trigger deserialization error
                                .param("pageSize", "10")
                                .param("pageNumber", "0"))
                .andExpect(status().isInternalServerError());
    }
}
