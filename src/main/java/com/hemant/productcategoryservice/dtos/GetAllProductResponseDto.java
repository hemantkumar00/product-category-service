package com.hemant.productcategoryservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class GetAllProductResponseDto {
    private Page<ProductResponseDto> products;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
