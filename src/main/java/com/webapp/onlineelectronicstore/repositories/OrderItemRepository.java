package com.webapp.onlineelectronicstore.repositories;

import com.webapp.onlineelectronicstore.entites.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

}