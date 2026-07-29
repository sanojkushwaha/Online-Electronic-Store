package com.webapp.onlineelectronicstore.controllers;

import com.webapp.onlineelectronicstore.dtos.response.ApiResponseMassage;
import com.webapp.onlineelectronicstore.dtos.response.CategoryDto;
import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.services.CategoryService;
import com.webapp.onlineelectronicstore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    // Create Category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {

        //creating categoryId randomly:
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);


        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Update Category
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
           @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable String categoryId) {

        CategoryDto updatedCategory =
                categoryService.updateCategory(categoryDto, categoryId);

        return ResponseEntity.ok(updatedCategory);
    }

    // Delete Category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMassage> deleteCategory(
            @PathVariable String categoryId) {

        categoryService.deleteCategory(categoryId);

        ApiResponseMassage response = ApiResponseMassage.builder()
                .message("Category deleted successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(response);
    }

    // Get All Categories
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(

            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        PageableResponse<CategoryDto> response =
                categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(response);
    }

    // Get Single Category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable String categoryId) {

        CategoryDto category = categoryService.getCategory(categoryId);

        return ResponseEntity.ok(category);
    }

    //create product with category
//    @PostMapping("{categoryId}/products")
//    public ResponseEntity<ProductDto> createProductWithCategory(
//            @PathVariable("categoryId") String categoryId,
//            @Valid @RequestBody ProductDto productDto
//    ){
//        ProductDto productwithCategory = productService.createWithCategory(productDto, categoryId);
//        return new ResponseEntity<>(productwithCategory,HttpStatus.CREATED);
//
//    }
}