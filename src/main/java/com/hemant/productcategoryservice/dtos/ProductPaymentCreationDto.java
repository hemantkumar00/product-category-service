package com.hemant.productcategoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPaymentCreationDto {
    private Long productId;
    private Double price;
    private String title;
    private String description;
    private OperationType operationType; // "ADD" or "REMOVE"
}
