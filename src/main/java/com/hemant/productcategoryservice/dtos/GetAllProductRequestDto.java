package com.hemant.productcategoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllProductRequestDto {
    private int pageNumber;
    private int pageSize;
}
