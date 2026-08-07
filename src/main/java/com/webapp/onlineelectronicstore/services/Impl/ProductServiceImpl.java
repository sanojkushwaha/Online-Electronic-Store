package com.webapp.onlineelectronicstore.services.Impl;

import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.dtos.response.ProductDto;
import com.webapp.onlineelectronicstore.entites.Category;
import com.webapp.onlineelectronicstore.entites.Product;
import com.webapp.onlineelectronicstore.exceptions.ResourceNotFoundException;
import com.webapp.onlineelectronicstore.helper.Helper;
import com.webapp.onlineelectronicstore.repositories.CategoryRepository;
import com.webapp.onlineelectronicstore.repositories.ProductRepository;
import com.webapp.onlineelectronicstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product = modelMapper.map(productDto, Product.class);
        //print productid
        System.out.println(product.getProductId());
        //added date
        product.setAddedDate(new Date());

        // Set Category
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with given id"));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setProductImage(productDto.getProductImage());
        product.setBrand(productDto.getBrand());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        product.setCategory(category);

        product.setCategory(category);

        product.setCategory(category);

        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteProduct(String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with given id"));

        productRepository.delete(product);
    }

    @Override
    public ProductDto getProduct(String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with given id"));

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber,
                                                       int pageSize,
                                                       String sortBy,
                                                       String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page = productRepository.findAll(pageable);

        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchProducts(String keyword,
                                                       int pageNumber,
                                                       int pageSize,
                                                       String sortBy,
                                                       String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page = productRepository
                .findByTitleContainingIgnoreCase(keyword, pageable);

        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProductsByCategory(String categoryId,
                                                                 int pageNumber,
                                                                 int pageSize,
                                                                 String sortBy,
                                                                 String sortDir) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with given id"));

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page = productRepository.findByCategory(category, pageable);

        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLiveProducts(int pageNumber,
                                                           int pageSize,
                                                           String sortBy,
                                                           String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page = productRepository.findByLive(true, pageable);

        return Helper.getPageableResponse(page, ProductDto.class);
    }

//    @Override
//    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
//        //fetch the category from db:
//        Category category =categoryRepository.findById(categoryId).orElseThrow(()->new  ResourceNotFoundException(
//                "Category " +
//                "not found " +
//                "with given id"));
//
//        Product product = modelMapper.map(productDto, Product.class);
//
//        //generrate random id
//        String productId = UUID.randomUUID().toString();
//        product.setProductId(productId);
//        product.setAddedDate(new Date());
//        //set category
//        product.setCategory(category);
//        //save the product in db
//        Product save = productRepository.save(product);
//
//        return modelMapper.map(save, ProductDto.class);
//    }
}