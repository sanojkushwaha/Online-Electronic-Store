package com.webapp.onlineelectronicstore.repositories;

import com.webapp.onlineelectronicstore.entites.Cart;
import com.webapp.onlineelectronicstore.entites.CartItem;
import com.webapp.onlineelectronicstore.entites.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {

    // Find a specific product in a cart
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}