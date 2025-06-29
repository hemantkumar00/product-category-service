package com.hemant.productcategoryservice.service.filteringService;

import com.hemant.productcategoryservice.models.Product;

import java.util.List;

public interface Filter {
    List<Product> apply(List<Product> products, List<String> allowedValues);
}
