package com.hemant.productcategoryservice.service.sortingService;

import com.hemant.productcategoryservice.models.Product;

import java.util.List;

public interface Sorter {
    List<Product> sort(List<Product> products);
}
