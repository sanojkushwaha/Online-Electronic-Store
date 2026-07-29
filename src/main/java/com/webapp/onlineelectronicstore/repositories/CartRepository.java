package com.webapp.onlineelectronicstore.repositories;

import com.webapp.onlineelectronicstore.entites.Cart;
import com.webapp.onlineelectronicstore.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    // Find cart by user
    Optional<Cart> findByUser(User user);

    // Find cart by userId
    Optional<Cart> findByUserUserId(String userId);

}