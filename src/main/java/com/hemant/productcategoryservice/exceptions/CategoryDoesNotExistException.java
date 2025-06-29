package com.hemant.productcategoryservice.exceptions;

public class CategoryDoesNotExistException extends RuntimeException {
    public CategoryDoesNotExistException(String message) {
        super(message);
    }
}
