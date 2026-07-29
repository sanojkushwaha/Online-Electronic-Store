package com.webapp.onlineelectronicstore.dtos.response;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String productId;

    @NotBlank(message = "Product title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Product description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount cannot be greater than 100")
    private double discount;

    @PositiveOrZero(message = "Discounted price cannot be negative")
    private double discountedPrice;

    @PositiveOrZero(message = "Quantity cannot be negative")
    private int quantity;

    @NotBlank(message = "Product image is required")
    private String productImage;

    @NotBlank(message = "Brand name is required")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    private String brand;

    private boolean live;

    private boolean stock;

    @NotBlank(message = "Category Id is required")
    private String categoryId;

    private  CategoryDto category;
}