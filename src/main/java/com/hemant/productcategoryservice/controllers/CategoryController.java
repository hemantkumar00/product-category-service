package com.hemant.productcategoryservice.controllers;

import com.hemant.productcategoryservice.dtos.CategoryResponseDto;
import com.hemant.productcategoryservice.dtos.GetAllCategoriesResponseDto;
import com.hemant.productcategoryservice.models.Category;
import com.hemant.productcategoryservice.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hemant.productcategoryservice.controllers.ProductController.from;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody Category category){
        Category createdCategory = categoryService.createCategory(category);
        CategoryResponseDto responseDto = from(createdCategory);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable("id") Long categoryId, @RequestBody Category category){
        Category updatedCategory = categoryService.updateCategory(categoryId, category);
        CategoryResponseDto responseDto = from(updatedCategory);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable("id") Long categoryId){
        boolean deleteResponse = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(deleteResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getSingleCategory(@PathVariable("id") Long categoryId) {
        Category category = categoryService.getSingleCategory(categoryId);
        CategoryResponseDto responseDto = from(category);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/")
    public ResponseEntity<GetAllCategoriesResponseDto> getAllCategories(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        GetAllCategoriesResponseDto responseDto = new GetAllCategoriesResponseDto();
        Page<Category> categoriesPage = categoryService.getAllCategories(pageNumber, pageSize);
        responseDto.setCategories(categoriesPage.map(ProductController::from));
        return ResponseEntity.ok(responseDto);
    }


}
