package com.webapp.onlineelectronicstore.controllers;

import com.webapp.onlineelectronicstore.dtos.request.AddItemToCartRequest;
import com.webapp.onlineelectronicstore.dtos.response.ApiResponseMassage;
import com.webapp.onlineelectronicstore.dtos.response.CartDto;
import com.webapp.onlineelectronicstore.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add Product To Cart
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable String userId,
            @Valid @RequestBody AddItemToCartRequest request) {

        CartDto cartDto = cartService.addItemToCart(userId, request);

        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    // Get User Cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(
            @PathVariable String userId) {

        CartDto cartDto = cartService.getCartByUser(userId);

        return ResponseEntity.ok(cartDto);
    }

    // Update Quantity
    @PutMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<CartDto> updateCartItem(
            @PathVariable String userId,
            @PathVariable String cartItemId,
            @RequestParam int quantity) {

        CartDto cartDto = cartService.updateItemQuantity(
                userId,
                cartItemId,
                quantity);

        return ResponseEntity.ok(cartDto);
    }

    // Remove Item From Cart
    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponseMassage> removeItemFromCart(
            @PathVariable String userId,
            @PathVariable String cartItemId) {

        cartService.removeItemFromCart(userId, cartItemId);

        ApiResponseMassage response = ApiResponseMassage.builder()
                .message("Item removed from cart successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(response);
    }

    // Clear Cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMassage> clearCart(
            @PathVariable String userId) {

        cartService.clearCart(userId);

        ApiResponseMassage response = ApiResponseMassage.builder()
                .message("Cart cleared successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(response);
    }
}