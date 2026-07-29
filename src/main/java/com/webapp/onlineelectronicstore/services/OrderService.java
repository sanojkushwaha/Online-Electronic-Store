package com.webapp.onlineelectronicstore.services;

import com.webapp.onlineelectronicstore.dtos.request.CreateOrderRequest;
import com.webapp.onlineelectronicstore.dtos.response.OrderDto;
import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.enums.OrderStatus;
import com.webapp.onlineelectronicstore.enums.PaymentStatus;

public interface OrderService {

    // Place Order
    OrderDto placeOrder(String userId, CreateOrderRequest request);

    // Get Order By Id
    OrderDto getOrder(String orderId);

    // Get All Orders Of User
    PageableResponse<OrderDto> getOrdersByUser(
            String userId,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    );

    // Get All Orders
    PageableResponse<OrderDto> getAllOrders(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    );

    // Update Order Status
    OrderDto updateOrderStatus(
            String orderId,
            OrderStatus status
    );

    // Update Payment Status
    OrderDto updatePaymentStatus(
            String orderId,
            PaymentStatus status
    );

    // Cancel Order
    void cancelOrder(String orderId);
}