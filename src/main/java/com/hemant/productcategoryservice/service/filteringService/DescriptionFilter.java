package com.hemant.productcategoryservice.service.filteringService;

import com.hemant.productcategoryservice.models.Product;

import java.util.List;

public class DescriptionFilter implements Filter {
    @Override
    public List<Product> apply(List<Product> products, List<String> allowedValues) {
        // Implement the filtering logic based on product description
        return products.stream()
                .filter(product -> allowedValues.stream()
                        .anyMatch(value -> product.getDescription().contains(value)))
                .toList();
    }
}
