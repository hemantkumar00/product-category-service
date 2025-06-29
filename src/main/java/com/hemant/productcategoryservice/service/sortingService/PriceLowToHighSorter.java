package com.hemant.productcategoryservice.service.sortingService;

import com.hemant.productcategoryservice.exceptions.NoProductsFoundException;
import com.hemant.productcategoryservice.models.Product;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PriceLowToHighSorter implements Sorter {
    @Override
    public List<Product> sort(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .collect(Collectors.toList());
    }
}
