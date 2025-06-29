package com.hemant.productcategoryservice.service.filteringService;

public class FilterFactory {
    public static Filter getFilterFromKey(String key) {
        return switch (key) {
            case "description" -> new DescriptionFilter();
            case "pricelessly" -> new PriceAndTitleFilter();
            default -> null;
        };
    }
}
