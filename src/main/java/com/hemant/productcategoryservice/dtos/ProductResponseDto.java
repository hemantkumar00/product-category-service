package com.hemant.productcategoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    Long id;
    private String title;
    private Double price;
    private String description;
    private CategoryResponseDto category;
    private String imgUrl;
}
