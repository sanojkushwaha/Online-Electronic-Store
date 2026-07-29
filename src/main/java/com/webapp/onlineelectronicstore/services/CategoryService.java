package com.webapp.onlineelectronicstore.services;

import com.webapp.onlineelectronicstore.dtos.response.CategoryDto;
import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;

public interface CategoryService {

    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //get all category
    PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single category detail
    CategoryDto getCategory(String categoryId);

    //search
}
