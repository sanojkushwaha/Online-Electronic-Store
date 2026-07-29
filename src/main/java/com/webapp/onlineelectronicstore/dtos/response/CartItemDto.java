package com.webapp.onlineelectronicstore.dtos.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private String cartItemId;

    @NotNull(message = "product is required ")
    @Valid
    private ProductDto product;

    @Min(value=1,message = "Quantity must be at least 1")
    private int quantity;

    @PositiveOrZero(message = "Total price cannot be negative")
    private double totalPrice;
}