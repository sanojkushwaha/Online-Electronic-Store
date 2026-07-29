package com.webapp.onlineelectronicstore.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemToCartRequest {

    @NotBlank(message = "Product Id is required")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}