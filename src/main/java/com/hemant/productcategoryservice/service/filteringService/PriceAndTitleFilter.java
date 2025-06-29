package com.hemant.productcategoryservice.service.filteringService;

import com.hemant.productcategoryservice.models.Product;

import java.util.List;

public class PriceAndTitleFilter implements Filter {
    @Override
    public List<Product> apply(List<Product> products, List<String> allowedValues) {
        // Implement the filtering logic based on product price and title
        return products.stream()
                .filter(product -> allowedValues.stream()
                        .anyMatch(value -> product.getTitle().contains(value) ||
                                          String.valueOf(product.getPrice()).contains(value)))
                .toList();
    }
}
