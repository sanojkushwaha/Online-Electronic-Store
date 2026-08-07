package com.webapp.onlineelectronicstore.dtos.response;

import com.webapp.onlineelectronicstore.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    private String cartId;

    @NotNull(message = "User is required !!")
    @Valid
    private UserDto user;

    @Builder.Default
    @NotEmpty(message = "cart must contain at least one item")
    @Valid
    private List<CartItemDto> items = new ArrayList<>();

    @PositiveOrZero(message = "cart total cannot be negative")
    private double cartTotal;

    private Role role;
}