package com.hemant.productcategoryservice.controllerAdvice;

import com.hemant.productcategoryservice.exceptions.CategoryAlreadyExistsException;
import com.hemant.productcategoryservice.exceptions.CategoryDoesNotExistException;
import com.hemant.productcategoryservice.exceptions.NoProductsFoundException;
import com.hemant.productcategoryservice.exceptions.ProductDoesNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductServiceExceptionHandler {

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<String> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException e) {
        return ResponseEntity.status(409).body("Category already exists: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(500).body("A runtime error occurred: " + e.getMessage());
    }

    @ExceptionHandler(CategoryDoesNotExistException.class)
    public ResponseEntity<String> handleCategoryDoesNotExistException(CategoryDoesNotExistException e) {
        return ResponseEntity.status(404).body("Category does not exist: " + e.getMessage());
    }

    @ExceptionHandler(NoProductsFoundException.class)
    public ResponseEntity<String> handleNoProductsFoundException(NoProductsFoundException e) {
        return ResponseEntity.status(404).body("No products found: " + e.getMessage());
    }

    @ExceptionHandler(ProductDoesNotExistException.class)
    public ResponseEntity<String> handleProductDoesNotExistException(ProductDoesNotExistException e) {
        return ResponseEntity.status(404).body("Product does not exist: " + e.getMessage());
    }
}
