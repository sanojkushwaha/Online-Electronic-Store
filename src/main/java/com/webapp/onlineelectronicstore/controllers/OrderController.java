package com.webapp.onlineelectronicstore.controllers;

import com.webapp.onlineelectronicstore.dtos.response.ApiResponseMassage;
import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.dtos.request.CreateOrderRequest;
import com.webapp.onlineelectronicstore.dtos.response.OrderDto;
import com.webapp.onlineelectronicstore.enums.OrderStatus;
import com.webapp.onlineelectronicstore.enums.PaymentStatus;
import com.webapp.onlineelectronicstore.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    //DI
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Place Order
    @PostMapping("/{userId}")
    public ResponseEntity<OrderDto> placeOrder(
            @PathVariable String userId,
            @Valid @RequestBody CreateOrderRequest request) {

        OrderDto order = orderService.placeOrder(userId, request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // Get Order By Id
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String orderId) {

        OrderDto order = orderService.getOrder(orderId);

        return ResponseEntity.ok(order);
    }

    // Get Orders Of User
    @GetMapping("/users/{userId}")
    public ResponseEntity<PageableResponse<OrderDto>> getOrdersByUser(
            @PathVariable String userId,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderDate") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {

        PageableResponse<OrderDto> response =
                orderService.getOrdersByUser(userId, pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(response);
    }

    // Get All Orders
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderDate") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir) {

        PageableResponse<OrderDto> response =
                orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(response);
    }

    // Update Order Status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status) {

        OrderDto order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }

    // Update Payment Status
    @PutMapping("/{orderId}/payment")
    public ResponseEntity<OrderDto> updatePaymentStatus(
            @PathVariable String orderId,
            @RequestParam PaymentStatus status) {

        OrderDto order = orderService.updatePaymentStatus(orderId, status);
        return ResponseEntity.ok(order);
    }

    // Cancel Order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMassage> cancelOrder(@PathVariable String orderId) {

        orderService.cancelOrder(orderId);

        ApiResponseMassage response = ApiResponseMassage.builder()
                .message("Order cancelled successfully.")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(response);
    }
}