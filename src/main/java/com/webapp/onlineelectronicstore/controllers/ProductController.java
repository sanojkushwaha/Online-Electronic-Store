package com.webapp.onlineelectronicstore.controllers;

import com.webapp.onlineelectronicstore.dtos.response.ApiResponseMassage;
import com.webapp.onlineelectronicstore.dtos.response.ImageResponse;
import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.dtos.response.ProductDto;
import com.webapp.onlineelectronicstore.services.FileService;
import com.webapp.onlineelectronicstore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    @Autowired //DI
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;


    // Create Product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductDto productDto) {

        ProductDto createdProduct = productService.createProduct(productDto);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // Update Product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @Valid @RequestBody ProductDto productDto,
            @PathVariable String productId) {

        ProductDto updatedProduct =
                productService.updateProduct(productDto, productId);

        return ResponseEntity.ok(updatedProduct);
    }

    // Delete Product
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMassage> deleteProduct(
            @PathVariable String productId) {

        productService.deleteProduct(productId);

        ApiResponseMassage response = ApiResponseMassage.builder()
                .message("Product deleted successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(response);
    }

    // Get Single Product
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(
            @PathVariable String productId) {

        ProductDto product = productService.getProduct(productId);

        return ResponseEntity.ok(product);
    }

    // Get All Products
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(

            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        PageableResponse<ProductDto> response =
                productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(response);
    }

    // Search Products
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        PageableResponse<ProductDto> response =
                productService.searchProducts(
                        keyword,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir);

        return ResponseEntity.ok(response);
    }

    // Get Products By Category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        PageableResponse<ProductDto> response =
                productService.getAllProductsByCategory(
                        categoryId,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir);

        return ResponseEntity.ok(response);
    }

    // Get Live Products
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getLiveProducts(

            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        PageableResponse<ProductDto> response =
                productService.getAllLiveProducts(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir);

        return ResponseEntity.ok(response);
    }

    //upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) throws IOException {

        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto product = productService.getProduct(productId);
        product.setProductImage(fileName);
        ProductDto productDto = productService.updateProduct(product, productId);

        ImageResponse response = ImageResponse.builder()
                .imageName(productDto.getProductImage())
                .message("Product Image Uploaded Successfully")
                .status(HttpStatus.CREATED)
                .success(true)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //serve image
    @GetMapping("/image/{productId}")
    public void serveProductImage(
            @PathVariable String productId,
            HttpServletResponse response) throws IOException {

        ProductDto productDto = productService.getProduct(productId);
        InputStream inputStream = fileService.getFile(imagePath, productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
}