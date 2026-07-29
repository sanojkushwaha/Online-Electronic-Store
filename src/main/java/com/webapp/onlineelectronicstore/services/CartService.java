package com.webapp.onlineelectronicstore.services;

import com.webapp.onlineelectronicstore.dtos.request.AddItemToCartRequest;
import com.webapp.onlineelectronicstore.dtos.response.CartDto;

public interface CartService {


    // Add product to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);
    // Remove product from cart
    void removeItemFromCart(String userId, String cartItemId);
    // Update quantity of a product
    CartDto updateItemQuantity(String userId, String cartItemId, int quantity);
    // Get cart by user
    CartDto getCartByUser(String userId);
    //clear cart
    void clearCart(String userId);
}