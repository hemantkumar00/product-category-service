package com.hemant.productcategoryservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class SearchResponseDto {
    Page<ProductResponseDto> products;
}
