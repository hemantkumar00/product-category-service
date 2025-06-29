package com.hemant.productcategoryservice.service.sortingService;

import com.hemant.productcategoryservice.dtos.SortingCriteria;

public class SorterFactory {

    public static Sorter getSortedByValue(SortingCriteria sortingCriteria) {
        return switch (sortingCriteria) {
            case PRICE_LOW_TO_HIGH -> new PriceLowToHighSorter();
            case PRICE_HIGH_TO_LOW -> new PriceHighToLowSorter();
        };
    }
}
