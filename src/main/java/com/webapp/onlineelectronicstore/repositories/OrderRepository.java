package com.webapp.onlineelectronicstore.repositories;

import com.webapp.onlineelectronicstore.entites.Order;
import com.webapp.onlineelectronicstore.entites.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    // Get all orders of a user
    List<Order> findByUser(User user);

    // Get paginated orders of a user
    Page<Order> findByUser(User user, Pageable pageable);

}