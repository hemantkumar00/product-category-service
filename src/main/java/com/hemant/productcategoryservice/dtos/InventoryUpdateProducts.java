package com.hemant.productcategoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class InventoryUpdateProducts {
    Map<Long, Integer> products = new HashMap<>();
}
