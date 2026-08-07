package com.webapp.onlineelectronicstore.services.Impl;

import com.webapp.onlineelectronicstore.dtos.response.CategoryDto;
import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.entites.Category;
import com.webapp.onlineelectronicstore.exceptions.ResourceNotFoundException;
import com.webapp.onlineelectronicstore.helper.Helper;
import com.webapp.onlineelectronicstore.repositories.CategoryRepository;
import com.webapp.onlineelectronicstore.services.CategoryService;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Builder
@Getter
@Setter
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        //convert Entity -->Dto= return Dto
        return  modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {

        //get category by Id
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not " + "found with given Id"));
        //update Category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        //save
        Category saveCategory = categoryRepository.save(category);

        return modelMapper.map(saveCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        //find Category if exist then delete
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not " + "found with given Id"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        PageableResponse<CategoryDto> pageableResponse =
                new ModelMapper().map(categoryPage, PageableResponse.class);

        return pageableResponse;
    }

    @Override
    public CategoryDto getCategory(String categoryId) {
       Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException(
               "Category not " + "found with given Id"));
        return modelMapper.map(category, CategoryDto.class);
    }
}
