package com.webapp.onlineelectronicstore.services;

import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.dtos.response.ProductDto;

public interface ProductService {

    // Create Product
    ProductDto createProduct(ProductDto productDto);

    // Update Product
    ProductDto updateProduct(ProductDto productDto, String productId);

    // Delete Product
    void deleteProduct(String productId);

    // Get Single Product
    ProductDto getProduct(String productId);

    // Get All Products
    PageableResponse<ProductDto> getAllProducts(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    );

    // Search Product by Title
    PageableResponse<ProductDto> searchProducts(
            String keyword,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    );

    // Get Products by Category
    PageableResponse<ProductDto> getAllProductsByCategory(
            String categoryId,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    );

    // Get Products by Live Status
    PageableResponse<ProductDto> getAllLiveProducts(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    );

    //create product with category
//    ProductDto createWithCategory(ProductDto productDto, String categoryId);

}