package com.hemant.productcategoryservice.service.sortingService;

import com.hemant.productcategoryservice.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class PriceHighToLowSorter implements Sorter {
    @Override
    public List<Product> sort(List<Product> products) {
        return products.stream()
                .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                .collect(Collectors.toList());
    }
}
